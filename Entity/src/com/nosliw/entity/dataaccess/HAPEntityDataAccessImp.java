package com.nosliw.entity.dataaccess;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.nosliw.common.configure.HAPConfigure;
import com.nosliw.common.exception.HAPServiceData;
import com.nosliw.common.utils.HAPBasicUtility;
import com.nosliw.common.utils.HAPConstant;
import com.nosliw.common.utils.HAPJsonUtility;
import com.nosliw.data.HAPData;
import com.nosliw.data1.HAPDataTypeManager;
import com.nosliw.entity.data.HAPDataWraper;
import com.nosliw.entity.data.HAPEntity;
import com.nosliw.entity.data.HAPEntityData;
import com.nosliw.entity.data.HAPEntityID;
import com.nosliw.entity.data.HAPEntityWraper;
import com.nosliw.entity.data.HAPReferenceInfoAbsolute;
import com.nosliw.entity.definition.HAPAttributeDefinition;
import com.nosliw.entity.definition.HAPEntityDefinitionSegment;
import com.nosliw.entity.definition.HAPEntityDefinitionManager;
import com.nosliw.entity.event.HAPEntityClearupEvent;
import com.nosliw.entity.event.HAPEvent;
import com.nosliw.entity.operation.HAPEntityOperationFactory;
import com.nosliw.entity.operation.HAPEntityOperationInfo;
import com.nosliw.entity.options.HAPOptionsDefinitionManager;
import com.nosliw.entity.query.HAPPageInfo;
import com.nosliw.entity.query.HAPQueryComponent;
import com.nosliw.entity.query.HAPQueryDefinition;
import com.nosliw.entity.query.HAPQueryDefinitionManager;
import com.nosliw.entity.query.HAPQueryManager;
import com.nosliw.entity.utils.HAPEntityDataUtility;
import com.nosliw.entity.utils.HAPEntityErrorUtility;
import com.nosliw.entity.utils.HAPTransactionUtility;

public abstract class HAPEntityDataAccessImp implements HAPEntityDataAccess{

	//data access under this data access 
	private HAPEntityDataAccess m_underMe;

	//store all the entity wrappers operated by this data access
	private HAPTransitionEntityManager m_transitionEntityMan;

	//manager everything about query
	protected HAPQueryManager m_queryManager;
	
	//manager everything about entity reference
	protected HAPReferenceManager m_referenceMan;
	
	//store all the operations related with request
	private HAPOperationAllResult m_results;

	private HAPConfigure m_configure;
	private HAPDataContext m_dataContext;
	
	//operation scope within this data access
	private int m_operationScope;
	
	public HAPEntityDataAccessImp(HAPConfigure configure, HAPEntityDataAccess access, HAPDataContext dataContext) {
		this.m_configure = configure;
		this.m_dataContext = dataContext;
		this.m_underMe = access;
	}

	//********************************  Lifecycle

	@Override
	public void destroy(){
		
	}

	//********************************  Entity  Operation
	
	@Override
	public HAPServiceData operate(HAPEntityOperationInfo operation){
		//init scope value of operation
		this.initOperationInfoScope(operation);
		
		//check whether valid operation scope
		if(-1==HAPTransactionUtility.getCurrentOperationScope(this.getOperationScope(), operation.getScope())){
			return HAPEntityErrorUtility.createEntityOperationInvalidScopeError(operation, "", null);
		}
		
		//check if valid operation within this data access
		HAPServiceData out = this.isValidOperation(operation);
		if(out.isSuccess()){
			//do something before real operation, for instance, back up data for reverse operation
			this.preOperate(operation);
			//do real operation
			out = this.doOperate(operation);
		}
		return out;
	}

	@Override
	public HAPServiceData isValidOperation(HAPEntityOperationInfo operation) {return HAPServiceData.createSuccessData();}
	
	
	abstract protected void preOperate(HAPEntityOperationInfo operation);
	
	@Override
	public int getOperationScope(){		return this.m_operationScope;	}

	@Override
	public void setOperationScope(int scope){	this.m_operationScope = scope;	}
	
	@Override
	public void openOperationResult(){	
		this.m_results = new HAPOperationAllResult();
		//query manager has seperate result set from data access
		this.m_queryManager.openOperationResult();
	}
	@Override
	public void addOperationResult(HAPEntityOperationInfo result){	this.m_results.addResult(result);	}
	@Override
	public void closeOperationResult(){ 
		this.m_results.clearup();
		this.m_queryManager.closeOperationResult();
	}
	@Override
	public HAPOperationAllResult getOperationResult(){	return this.m_results;	}


	protected HAPServiceData doOperate(HAPEntityOperationInfo operation){
		HAPServiceData out = HAPServiceData.createFailureData();
		switch(operation.getOperation()){
		case ENTITYOPERATION_ATTR_ATOM_SET:
		case ENTITYOPERATION_ATTR_CRITICAL_SET:
		case ENTITYOPERATION_ATTR_ELEMENT_DELETE:
		case ENTITYOPERATION_ATTR_ELEMENT_NEW:
		case ENTITYOPERATION_ATTR_REFERENCE_SET:
		case ENTITYOPERATION_ATTR_REFERENCE_CLEAR:
		{
			//get attribute wrapper
			HAPEntityWraper entityWraper = (HAPEntityWraper)useEntityByID(operation.getEntityID()).getData(); 
			HAPDataWraper attrWraper = entityWraper.getChildWraperByPath(operation.getAttributePath());
			//prepare informaton for reverse operation 
			attrWraper.prepareReverseOperation(operation, out);
			//do operation on attribute wrapper
			out = attrWraper.operate(operation);
			//add root entity as changed entity
			this.addTransitEntity(entityWraper.getRootEntityWraper(), HAPConstant.DATAACCESS_ENTITYSTATUS_CHANGED);
			//for global scope operation, add root entity as changed entity to operation result for future query update
			if(operation.getScope()==HAPConstant.ENTITYOPERATION_SCOPE_GLOBAL)		this.getOperationResult().addEntity(entityWraper.getRootEntityWraper(), HAPConstant.DATAACCESS_ENTITYSTATUS_CHANGED);
			break;
		}
		case ENTITYOPERATION_ENTITY_OPERATIONS:
		{
			//running operation one by one
			for(HAPEntityOperationInfo operationEle : operation.getEntityOperations()){
				//each is auto commited
				operation.setIsAutoCommit(true);
				out = this.operate(operation);
				//for any error, stop running rest, the operation fails with the error
				if(out.isFail()) 	break;
			}
			break;
		}
		case ENTITYOPERATION_ENTITY_NEW:
		{
			HAPEntityWraper entityWraper = null;
			HAPEntityID entityId = operation.getEntityID();
			Map<String, String> parms = operation.getParms();

			//create new entity wrapper
			List<HAPEntityOperationInfo> initOps = new ArrayList<HAPEntityOperationInfo>();
			if(entityId!=null)			entityWraper = newRootEntityWraper(entityId, parms, initOps);
			else{
				entityWraper = newRootEntityWraper(operation.getEntityType(), null, parms, initOps);
				operation.setEntityID(entityWraper.getID());
			}
			
			this.addTransitEntity(entityWraper, HAPConstant.DATAACCESS_ENTITYSTATUS_NEW);
			//store entity wrapper to extra in operation, so that client side can use extra value to create wrapper
			operation.setExtra(entityWraper.toStringValue(HAPSerializationFormat.JSON));
			out = HAPServiceData.createSuccessData(entityWraper);
			if(operation.getScope()==HAPConstant.ENTITYOPERATION_SCOPE_GLOBAL){
				this.onEvent(HAPEvent.createEntityNewEvent(entityWraper));
				this.getOperationResult().addEntity(entityWraper, HAPConstant.DATAACCESS_ENTITYSTATUS_NEW);
			}

			//run extra operations
			for(HAPEntityOperationInfo initOp : initOps)	this.operate(initOp);
			
			break;
		}
		case ENTITYOPERATION_ENTITY_DELETE:
		{
			HAPEntityWraper entityWraper = (HAPEntityWraper)this.useEntityByID(operation.getEntityID()).getData();
			out = entityWraper.preRemove();
			if(out.isSuccess()){
				//back up entity wrapper to extra attribute in operation
				operation.setExtra(entityWraper.cloneWraper());
				entityWraper.setStatus(HAPConstant.DATAACCESS_ENTITYSTATUS_DEAD);
				entityWraper.clearUp(operation.getScope());
				this.addTransitEntity(entityWraper, HAPConstant.DATAACCESS_ENTITYSTATUS_DEAD);
				if(operation.getScope()==HAPConstant.ENTITYOPERATION_SCOPE_GLOBAL)		this.getOperationResult().addEntity(entityWraper, HAPConstant.DATAACCESS_ENTITYSTATUS_DEAD);
				out = HAPServiceData.createSuccessData(entityWraper);
			}
			break;
		}
		case ENTITYOPERATION_REFERENCE_REMOVE:
		{
			HAPReferenceInfoAbsolute refPath = operation.getReferencePath();
			HAPEntityWraper entityWraper = (HAPEntityWraper)this.useEntityByID(operation.getEntityID()).getData();
			this.getReferenceManager().removeParentReference(entityWraper.getID(), refPath.getEntityID(), refPath.getAttrPath());
			break;
		}
		case ENTITYOPERATION_REFERENCE_ADD:
		{
			HAPReferenceInfoAbsolute refPath = operation.getReferencePath();
			HAPEntityWraper entityWraper = (HAPEntityWraper)this.useEntityByID(operation.getEntityID()).getData();
			getReferenceManager().addParentReference(entityWraper.getID(), refPath.getEntityID(), refPath.getAttrPath());
			break;
		}
		case ENTITYOPERATION_ENTITYATTR_ADD:
		{
			HAPAttributeDefinition attrDef = operation.getAttributeDefinition();
			HAPEntityWraper entityWraper = (HAPEntityWraper)this.useEntityByID(operation.getEntityID()).getData();

			HAPEntityDefinitionSegment entityDef = entityWraper.getEntityData().getEntityInfo();
			entityDef.copyAttributeDefinition(attrDef);

			HAPEntity entityDataType = (HAPEntity)HAPEntityDataUtility.getEntityDataType(entityWraper.getEntityType(), this.getDataTypeManager());
			HAPDataWraper attrWraper = entityDataType.newAttributeWraper(attrDef, entityWraper.getEntityData());
			operation.setExtra(attrWraper.toStringValue(HAPSerializationFormat.JSON));
			out = HAPServiceData.createSuccessData(attrWraper);
			break;
		}
		case ENTITYOPERATION_ENTITYATTR_REMOVE:
		{
			HAPEntityWraper entityWraper = (HAPEntityWraper)this.useEntityByID(operation.getEntityID()).getData();
			HAPDataWraper attrWraper = (HAPDataWraper)entityWraper.getChildWraperByPath(operation.getAttributePath()).cloneWraper();
			operation.setExtra(attrWraper);

			HAPEntityDefinitionSegment entityDef = entityWraper.getEntityData().getEntityInfo();
			entityDef.removeAttributeDefinition(operation.getAttributePath());
			
			entityWraper.getEntityData().removeAttribute(operation.getAttributePath(), operation.getScope());
			out = HAPServiceData.createSuccessData();
			break;
		}
//		case ENTITYOPERATION_QUERY_ENTITY_ADD:
//		case ENTITYOPERATION_QUERY_ENTITY_REMOVE:
//		case ENTITYOPERATION_QUERY_ENTITY_MODIFY:
//		{
//			HAPQueryComponent queryComponent = this.getQueryComponent(operation.getQueryName());
//			if(queryComponent!=null){
//				queryComponent.operate(operation);
//			}
//			break;
//		}
		}
		return out;
	}

	/*
	 * init operation scope
	 * 		if not set, then use scope of this data access
	 */
	protected void initOperationInfoScope(HAPEntityOperationInfo operation){
		if(operation.getScope()==-1){
			operation.setScope(this.getOperationScope());
		}
	}
	
	protected HAPEntityWraper newRootEntityWraper(HAPEntityID entityID, Map<String, String> parms, List<HAPEntityOperationInfo> initOps){
		return this.newRootEntityWraper(entityID.getEntityType(), entityID.getId(), parms, initOps);
	}
	
	/*
	 * create root entity wrapper
	 * 		type : 		entity type
	 * 		id :   		entity id
	 * 		parms : 	in the format of attribute --- value, used to set attribute value during entity consturctor
	 * 		initOps : 	container used to store extra operation after entity is constructed
	 */
	protected HAPEntityWraper newRootEntityWraper(String type, String id, Map<String, String> parms, List<HAPEntityOperationInfo> initOps){
		//create new entity
		HAPEntity entityDataType = HAPEntityDataUtility.getEntityDataType(type, this.getDataTypeManager());
		HAPEntityData entityData = entityDataType.newEntity();
		//create wrapper
		HAPEntityWraper out = new HAPEntityWraper(entityData, this.getDataTypeManager(), this.getEntityDefinitionManager());
		//set ID
		String entityId = HAPBasicUtility.isStringEmpty(id)?this.getNextEntityId():id; 
		out.setId(entityId);
		out.setID(new HAPEntityID("", type, entityId));
		out.setStatus(HAPConstant.DATAACCESS_ENTITYSTATUS_NEW);
		out.setDataAccess(this);
		
		//init entity data, get extra operation
		List<HAPEntityOperationInfo> ops = entityData.init(parms);
		initOps.addAll(ops);
		return out;
	}
	
	@Override
	public void submitOperation(HAPEntityOperationInfo op) {
		HAPEntityOperationInfo operation = op.cloneOperationInfo();
		
		int scope = HAPTransactionUtility.getCurrentOperationScope(this.getOperationScope(), operation.getScope());
		operation.setScope(scope);

		this.operate(operation);
	}

	@Override
	public HAPServiceData preCommit(){
		return HAPServiceData.createSuccessData();
	}
	
	@Override
	public HAPServiceData postCommit(){
		return HAPServiceData.createSuccessData();
	}

	
	//********************************  Entity  Management
	@Override
	public HAPServiceData useEntityByID(HAPEntityID ID){
		return this.getEntityByID(ID, true);
	}

	@Override
	public HAPServiceData readEntityByID(HAPEntityID ID){
		return this.getEntityByID(ID, false);
	}

	@Override
	public HAPServiceData getEntityByID(HAPEntityID ID, boolean ifKeep){
		HAPServiceData out = null;
		HAPEntityWraper entityWraper = null;
		if(HAPTransactionUtility.isEntityBelongsToUserContext(ID)){
			//standarlize ID
			HAPEntityID uniformID = ID.getUniformEntityID();
			//get root ID
			HAPEntityID rootEntityID = uniformID.getRootEntityID();
			//get root entity wrapper
			HAPEntityWraper rootEntityWraper = this.getUserContextEntityByID(rootEntityID, ifKeep);
			
			//get entity wrapper by path
			entityWraper = (HAPEntityWraper)rootEntityWraper.getChildWraperByPath(uniformID.getAttributePath());

			if(entityWraper==null)  out = HAPServiceData.createFailureData();
			else out = HAPServiceData.createSuccessData(entityWraper);
		}
		else{
//			out = this.getUserContext().getExternalEntity(ID);
		}
		return out;
	}
	
	abstract protected HAPEntityWraper getUserContextEntityByID(HAPEntityID ID, boolean ifKeep);

	protected void addTransitEntity(HAPEntityWraper entityWraper, int status){
		this.m_results.addEntity(entityWraper, status);
	}
	
	protected HAPEntityWraper getTransitEntity(HAPEntityID ID){
		return this.m_transitionEntityMan.getEntityByID(ID);
	}
	
	protected Set<HAPEntityWraper> getTransitEntitys(int status){
		return this.m_transitionEntityMan.getAllEntitys(status);
	}
	
	
	@Override
	public Set<HAPEntityWraper> getAllTransitEntitys(){
		Set<HAPEntityWraper> out = new HashSet<HAPEntityWraper>();
		out.addAll(this.m_results.getAllEntitys(HAPConstant.DATAACCESS_ENTITYSTATUS_NORMAL));
		out.addAll(this.m_results.getAllEntitys(HAPConstant.DATAACCESS_ENTITYSTATUS_CHANGED));
		out.addAll(this.m_results.getAllEntitys(HAPConstant.DATAACCESS_ENTITYSTATUS_NEW));
		return out;
	}
	
	@Override
	public Set<HAPEntityWraper> getTransitEntitysByType(String type){
		Set<HAPEntityWraper> out = new HashSet<HAPEntityWraper>();
		out.addAll(this.m_results.getEntityContainer(HAPConstant.DATAACCESS_ENTITYSTATUS_NORMAL).getAllEntityByType(type));
		out.addAll(this.m_results.getEntityContainer(HAPConstant.DATAACCESS_ENTITYSTATUS_CHANGED).getAllEntityByType(type));
		out.addAll(this.m_results.getEntityContainer(HAPConstant.DATAACCESS_ENTITYSTATUS_NEW).getAllEntityByType(type));
		return out;
	}
	
	@Override
	public Set<HAPEntityWraper> getTransitEntitysByStatus(int status){
		return this.m_transitionEntityMan.getAllEntitys(status);
	}
	

	
	//********************************  Query Management
	@Override
	public HAPQueryComponent queryRequest(HAPQueryDefinition query, Map<String, HAPData> queryParms, HAPPageInfo pageInfo){
		return this.m_queryManager.query(query, queryParms, pageInfo, true);
	}

	@Override
	public HAPQueryComponent queryRequest(String queryId, HAPPageInfo pageInfo){
		return this.m_queryManager.query(queryId, pageInfo, true);
	}
	
	@Override
	public void updateQueryByResult(){
		for(HAPEntityWraper wraper : this.m_results.getAllEntitys(HAPConstant.DATAACCESS_ENTITYSTATUS_CHANGED)){
			this.m_queryManager.changeEntity(wraper);
		}
		for(HAPEntityWraper wraper : this.m_results.getAllEntitys(HAPConstant.DATAACCESS_ENTITYSTATUS_NEW)){
			this.m_queryManager.changeEntity(wraper);
		}
		for(HAPEntityWraper wraper : this.m_results.getAllEntitys(HAPConstant.DATAACCESS_ENTITYSTATUS_DEAD)){
			this.m_queryManager.changeEntity(wraper);
		}
		this.m_results.clearUpdatedEntitys();
	}
	
	@Override
	public void removeQuery(String queryId){	this.m_queryManager.removeQuery(queryId);	}
	
	@Override
	public Set<String> getAllQuerys(){		return this.m_queryManager.getAllQuerys();	}
	
	@Override
	public HAPQueryComponent getQueryComponent(String id) {		return this.m_queryManager.getQuery(id);	}

	
	//********************************  Reference Management
	protected void mergeReference(){
		HAPEntityDataAccess under = this.getUnderDataAccess();
		if(under!=null){
			under.getReferenceManager().mergeWith(this.getReferenceManager());
		}		
	}

	@Override
	public void clearReferenceToEntity(HAPEntityID entityID){
		Set<HAPReferenceInfoAbsolute> references = getReferenceManager().getParentReferences(entityID);
		for(HAPReferenceInfoAbsolute ref : references){
			HAPEntityOperationInfo operation = HAPEntityOperationFactory.createAttributeReferenceClearOperation(ref.getEntityID(), ref.getAttrPath());
			this.operate(operation);
		}
	}
	
	@Override
	public void breakEntityReference(HAPReferenceInfoAbsolute referencePath, HAPEntityID ID){
		HAPEntityOperationInfo operation = HAPEntityOperationFactory.createReferenceRemoveOperation(ID, referencePath);
		this.operate(operation);
	}
		
	@Override
	public void buildEntityReference(HAPReferenceInfoAbsolute referencePath, HAPEntityID ID){
		HAPEntityOperationInfo operation = HAPEntityOperationFactory.createReferenceAddOperation(ID, referencePath);
		this.operate(operation);
	}

	
	
	
	//********************************  Basic Information

	@Override
	public HAPEntityDataAccess getUnderDataAccess(){	return this.m_underMe;	}
	@Override
	public HAPQueryManager getQueryManager(){ return this.m_queryManager; }
	@Override
	public HAPTransitionEntityManager getTransitionEntityManager(){ return this.m_transitionEntityMan; }
	@Override
	public HAPReferenceManager getReferenceManager(){return this.m_referenceMan;}
	
	private String getNextEntityId(){	return System.currentTimeMillis()+"";	}
	
	
	protected HAPDataTypeManager getDataTypeManager(){return this.m_dataContext.getDataTypeManager();}
	protected HAPEntityDefinitionManager getEntityDefinitionManager(){return this.m_dataContext.getEntityDefinitionManager();}
	protected HAPOptionsDefinitionManager getOptionsManager(){return this.m_dataContext.getOptionsManager();}
	protected HAPQueryDefinitionManager getQueryDefinitionManager(){return this.m_dataContext.getQueryDefinitionManager();}
	protected HAPDataContextInfo getUserEnvInfo(){ return this.m_dataContext.getDataContextInfo(); }
	
	public HAPDataContext getDataContext(){ return this.m_dataContext; }
	public void setDataContext(HAPDataContext dataContext){ this.m_dataContext = dataContext; }

	protected HAPConfigure getConfigure(){ return this.m_configure; }
	
	@Override
	public String toStringValue(String format){
		Map<String, String> jsonDataMap = new LinkedHashMap<String, String>();
//		jsonDataMap.put("NormalEntity", this.getEntityNormalContainer().toStringValue(format));
//		jsonDataMap.put("ModifiedEntity", this.getEntityChangedContainer().toStringValue(format));
//		jsonDataMap.put("DeadEntity", this.getEntityDeadContainer().toStringValue(format));
//		jsonDataMap.put("NewEntity", this.getEntityNewContainer().toStringValue(format));
		
		jsonDataMap.put("EntityReference", this.getReferenceManager().toStringValue(format));
		
		Map<String, String> jsonQueryMap = new LinkedHashMap<String, String>();
		for(String name : this.getAllQuerys()){
			jsonQueryMap.put(name, this.getQueryComponent(name).toStringValue(format));
		}
		
		return HAPJsonUtility.buildMapJson(jsonDataMap);
	}
	
	
	@Override
	public void onEvent(HAPEvent event){
//		this.informQuery(event);
		
		switch(event.getType()){
		case HAPConstant.EVENTTYPE_ENTITY_CLEARUP:
			this.clearReferenceToEntity(((HAPEntityClearupEvent)event).getEntityWraper().getID());
			break;
		}
	}

	
	
	
	
	
	
/*	
	
	@Override
	public HAPQueryComponent queryEntityComponent(HAPQueryDefinition query, Map<String, HAPData> queryParms, HAPPageInfo pageInfo) {
		
		List<HAPQueryEntityWraper> out = new ArrayList<HAPQueryEntityWraper>();

		HAPEntityDataAccess under = this.getUnderDataAccess();
		this.m_queryManager.query(queryDef, queryParms);
		
		List<HAPEntityWraper> entityWrapers = this.queryEntityWrapers(query);
		
		HAPQueryComponent queryComponent = new HAPQueryComponent(this, this.getDataTypeManager());
		queryComponent.setQueryInfo(query);

		for(HAPEntityWraper entity : entityWrapers){
			queryComponent.addEntityWraper(entity);
		}
		this.addQueryComponent(queryComponent);
		
		return queryComponent;
	}
	
	@Override
	public List<HAPEntityWraper> queryEntityWrapers(HAPQueryDefinition query){
		List<HAPEntityWraper> out = new ArrayList<HAPEntityWraper>();
		
		HAPEntityDataAccess under = this.getUnderDataAccess();
		if(under!=null)		out.addAll(under.queryEntityWrapers(query));
		out.addAll(this.getEntityNormalContainer().query(query));
		out.addAll(this.getEntityChangedContainer().query(query));
		out.addAll(this.getEntityNewContainer().query(query));
		
		for(HAPEntityWraper entity : this.getEntityDeadContainer().getAllTransitEntitys()){
			out.remove(entity);
		}
		
		List<HAPEntityWraper> listOut = new ArrayList<HAPEntityWraper>();
		for(HAPEntityWraper entity : out) listOut.add(entity);
		return listOut;
	}
	
	protected void informQuery(HAPEvent event){
		for(String name : this.getAllQuerys()){
			this.getQueryComponent(name).onEvent(event);
		}
	}
	

	protected void addQueryComponent(HAPQueryComponent query){
		String queryId = query.getQueryInfo().getQueryId();
		query.setTransDataAccess(this);
		this.m_querys.put(queryId, query);
	}
	
	protected void mergeQuery(){
		HAPEntityDataAccess under = this.getUnderDataAccess();
		if(under!=null){
			for(String name : this.getAllQuerys()){
				HAPQueryComponent underQuery = under.getQueryComponent(name);
				if(underQuery!=null){
					underQuery.addQueryEntityWrapers(this.getQueryComponent(name).getEntitys());
				}
			}
		}
	}
	
*/
	
	
//	protected void handleNewEntityReference(HAPEntityWraper entityWraper){
//		//for new entity, update reference
//		HAPValueUtility.iterateEntityWraper(entityWraper, null, new HAPDataWraperTask(){
//			@Override
//			public HAPServiceData process(HAPDataWraper wraper, Object data) {
//				HAPServiceData out = HAPServiceData.createSuccessData();
//				m_referenceMan.addParentReference(refWraper);
//				addReferenceForEntity(wraper);
//				return out;
//			}
//			@Override
//			public Object afterProcess(HAPDataWraper wraper, Object data) {	return null;}
//		});
//	}
//	
//	protected void addReferenceForEntity(HAPDataWraper wraper){
//		if(wraper.isEmpty())  return;
//		HAPData eleData = wraper.getData();
//		if(eleData instanceof HAPReferenceWraperData){
//			HAPReferenceWraperData refData = (HAPReferenceWraperData)eleData;
//
//			HAPEntityID ID = refData.getReferenceID();
//			if(ID==null)  return;
//
//			HAPEntityWraper entityWraper = (HAPEntityWraper)getEntityByID(ID).getSuccessData();
//			HAPReferencePath path = new HAPReferencePath(wraper.getParentEntity().getWraper().getID(), wraper.getParentEntityAttributePath());
//			entityWraper.addReferencePath(path);
//		}
//	}
	
//	@Override
//	public HAPUserContext getCurrentUserContext(){	return this.getUserContext();	}

	
}
