package com.nosliw.entity.persistent.mango;

import org.bson.Document;
import org.bson.types.ObjectId;

import com.mongodb.BasicDBObject;
import com.mongodb.client.MongoCollection;
import com.mongodb.client.MongoDatabase;
import com.nosliw.common.configure.HAPConfigure;
import com.nosliw.common.utils.HAPConstant;
import com.nosliw.entity.data.HAPEntityID;
import com.nosliw.entity.data.HAPEntityWraper;
import com.nosliw.entity.dataaccess.HAPEntityPersistent;
import com.nosliw.entity.dataaccess.HAPOperationAllResult;
import com.nosliw.entity.dataaccess.HAPReferenceManager;
import com.nosliw.entity.operation.HAPEntityOperationInfo;

public class HAPPersistentDataAccess extends HAPEntityPersistent{

	private MongoDatabase m_database;
	
	public HAPPersistentDataAccess(HAPConfigure configure) {
		super(configure);
	}

	@Override
	public void init() {
		this.m_queryManager = new HAPPersistentQueryManager(this);
		this.m_referenceMan = new HAPReferenceManager(this);
	}

	@Override
	public HAPOperationAllResult commit() {
		for(HAPEntityWraper deadEntity : this.getTransitEntitys(HAPConstant.DATAACCESS_ENTITYSTATUS_DEAD)){
			this.deadEntity(deadEntity);
		}

		for(HAPEntityWraper newEntity : this.getTransitEntitys(HAPConstant.DATAACCESS_ENTITYSTATUS_NEW)){
			this.newEntity(newEntity);
		}
		
		for(HAPEntityWraper changedEntity : this.getTransitEntitys(HAPConstant.DATAACCESS_ENTITYSTATUS_CHANGED)){
			this.changedEntity(changedEntity);
		}
		
		return null;
	}

	@Override
	protected HAPEntityWraper getUserContextEntityByID(HAPEntityID ID, boolean ifKeep) {
		
		MongoCollection<Document> collection = this.m_database.getCollection(ID.getEntityType());
		
	    BasicDBObject query = new BasicDBObject();
	    query.put("_id", new ObjectId(ID.toString()));
	    Document dbObj = collection.find(query).first();
	    
	    HAPEntityWraper entity = HAPEntityImportUtil.importEntity(dbObj, ID.getEntityType(), this.getEntityDefinitionManager(), this.getDataTypeManager());
	    return entity;
	}

	@Override
	protected void preOperate(HAPEntityOperationInfo operation) {}

	
	private void deadEntity(HAPEntityWraper entity){
		MongoCollection<Document> collection = this.m_database.getCollection(entity.getEntityType());
	    BasicDBObject query = new BasicDBObject();
	    query.put("_id", new ObjectId(entity.getID().toString()));
		collection.deleteOne(query);
	}
	
	private void newEntity(HAPEntityWraper entity){
		String collection = entity.getEntityType();
		Document doc = this.createDocumentByEntity(entity);
		this.m_database.getCollection(collection).insertOne(doc);
	}

	private void changedEntity(HAPEntityWraper entity){
		String collection = entity.getEntityType();
		Document doc = this.createDocumentByEntity(entity);
		this.m_database.getCollection(collection).replaceOne(new Document("entityID", entity.getID().toString()), this.createDocumentByEntity(entity));
	}
	
	private Document createDocumentByEntity(HAPEntityWraper entity){
		Document doc = HAPEntityExportUtil.exportEntity(entity);
		return doc;
	}
	
	private String getId(HAPEntityWraper entity){
		return entity.getID().toString();
	}
}
