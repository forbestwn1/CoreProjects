package com.nosliw.entity.query;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.nosliw.common.serialization.HAPStringable;
import com.nosliw.common.utils.HAPConstant;
import com.nosliw.data.HAPData;
import com.nosliw.data.HAPDataTypeManager;
import com.nosliw.entity.data.HAPEntity;
import com.nosliw.entity.data.HAPEntityID;
import com.nosliw.entity.data.HAPEntityWraper;
import com.nosliw.entity.dataaccess.HAPEntityDataAccess;
import com.nosliw.entity.event.HAPEntityClearupEvent;
import com.nosliw.entity.event.HAPEntityModifyEvent;
import com.nosliw.entity.event.HAPEntityNewEvent;
import com.nosliw.entity.event.HAPEvent;
import com.nosliw.entity.event.HAPEventListener;
import com.nosliw.entity.operation.HAPEntityOperationFactory;
import com.nosliw.entity.operation.HAPEntityOperationInfo;

/*
 * a class for container of query entity (query result)
 * 	
 */
public class HAPQueryComponent implements HAPStringable{

	//each query (with same query def and query parm) have unique id
	protected String m_queryId;
	//query definition
	protected HAPQueryDefinition m_queryDefinition;
	//parms used in query
	protected Map<String, HAPData> m_queryParms;
	
	//query entity id list for query result
	protected List<String> m_entityIdList;
	//query entity data for query result mapped by 
	protected Map<String, HAPQueryEntityWraper> m_entitys;
	//for entity ID, which query entitys are affected
	protected Map<String, Set<String>> m_entityIDToQueryId;
	
	//describe current query entity segment within this query component
	protected HAPQuerySegmentInfo m_segmentInfo;

	//whether affected query entitys should be validated again
	protected boolean m_revalidation = false;
	
	//whether affected query entitys should be updated
	protected boolean m_update = false;
	
	protected HAPQueryManager m_queryMan;
	protected HAPDataTypeManager m_dataTypeMan;
	
	public HAPQueryComponent(String queryId, HAPQueryDefinition queryDef, Map<String, HAPData> queryParms, HAPQueryManager queryManager, HAPDataTypeManager dataTypeMan){
		this.m_queryMan = queryManager;
		this.m_dataTypeMan = dataTypeMan;
		this.m_queryId = queryId;
		this.m_queryDefinition = queryDef;
		this.m_queryParms = queryParms;

		this.m_entityIdList = new ArrayList<String>();
		this.m_entitys = new LinkedHashMap<String, HAPQueryEntityWraper>();
		this.m_entityIDToQueryId = new LinkedHashMap<String, Set<String>>();
	}
	
	public void clearup(){};
	
	
	/*
	 * handle new entity
	 */
	public HAPEntityOperationInfo newEntity(HAPEntityWraper entity){	return null;	}

	public HAPEntityOperationInfo deleteEntity(HAPEntityID ID){
		return null;
	}

	public HAPEntityOperationInfo changeEntity(HAPEntityWraper entity){	return null; }
	
	/*
	 * method to process entity object, 
	 * if query valid entity object, then return query entity
	 * otherwise return null
	 */
	protected HAPQueryEntityWraper processEntity(HAPEntityWraper entity){
		if(this.m_queryDefinition.isRelatedEntity(entity)){
			if(this.isValidEntity(entity)){
				HAPQueryEntityWraper queryEntityWraper = this.createQueryEntityWraper(entity);
				return queryEntityWraper;
			}
		}
		return null;
	}

	/*
	 * check if entity is valid entity for query
	 */
	protected boolean isValidEntity(HAPEntityWraper entity){
		Map<String, HAPEntityWraper> entityWrapers = new LinkedHashMap<String, HAPEntityWraper>();
		for(HAPQueryProjectAttribute entityAttr : this.m_queryDefinition.getEntityAttributes()){
			entityWrapers.put(entityAttr.entityName, entity);
		}
		return this.m_queryDefinition.isValidEntity(entityWrapers, m_queryParms);
	}
	
	/*
	 * method to process entity object and created responding query entity according to attribute required in query definition
	 */
	protected HAPQueryEntityWraper createQueryEntityWraper(HAPEntityWraper entity){
		HAPQueryEntityWraper queryWraper = new HAPQueryEntityWraper(
				this.getQueryDefinition().getEntityAttributes(), 
				this.m_dataTypeMan);
		
		Map<String, HAPEntityWraper> entityWrapers = new LinkedHashMap<String, HAPEntityWraper>();
		for(HAPQueryProjectAttribute entityAttr : this.m_queryDefinition.getEntityAttributes()){
			entityWrapers.put(entityAttr.entityName, entity);
		}
		queryWraper.process(entityWrapers, this.getEntityDataAccess());
		return queryWraper;
	}
	
	/*
	 * this method is call when some entity is supposed to removed from this container
	 * call this method to 
	 * 		1.  try to update information on query side
	 *      2.  create operation info for this update
	 */
	protected HAPEntityOperationInfo removeQueryEntityOperation(String queryEntityId, int scope){
		HAPEntityOperationInfo operation = null;
		if(this.m_entityIdList.contains(queryEntityId)){
			this.removeQueryEntityById(queryEntityId);
			operation = HAPEntityOperationFactory.createQueryRemoveEntityOperation(this.getQueryId(), queryEntityId);
			if(scope!=HAPConstant.CONS_ENTITYOPERATION_SCOPE_UNDEFINED)  operation.setScope(scope);   
		}
		return operation;
	}
	
	/*
	 * this method is call when some entity is suposed to add to this container
	 * call this method to 
	 * 		1.  try to update information on query side
	 *      2.  create operation info for this update
	 */
	protected HAPEntityOperationInfo addQueryEntityOperation(HAPQueryEntityWraper queryEntityWraper, int scope){
		HAPEntityOperationInfo operation = null;
		
		int position = this.addQueryEntityWraper(queryEntityWraper);
		if(this.m_segmentInfo.ifWithinSegment(position)){
			operation = HAPEntityOperationFactory.createQueryAddEntityOperation(this.getQueryId(), position, queryEntityWraper);
			if(scope!=HAPConstant.CONS_ENTITYOPERATION_SCOPE_UNDEFINED)  operation.setScope(scope);   
		}
		
		return operation;
	}

	/*
	 * this method is call when some query valid entity is modified
	 * call this method to 
	 * 		1.  try to update information on query side
	 *      2.  create operation info for this update
	 */
	protected HAPEntityOperationInfo addOrUpdateQueryEntityOperation(HAPQueryEntityWraper queryEntityWraper, int scope){
		HAPEntityOperationInfo operation = null;
		String id = queryEntityWraper.getId();
		if(this.m_entityIdList.contains(id)){
			//already there, just update
			HAPQueryEntityWraper oldData = this.m_entitys.get(id);
			
			//remove old wraper
			int oldIndex = this.m_entityIdList.indexOf(id);
			removeQueryEntityById(id);
			
			//insert back
			int newIndex = this.addQueryEntityWraper(queryEntityWraper);
			if(this.m_segmentInfo.ifWithinSegment(newIndex)){
				operation = HAPEntityOperationFactory.createQueryModifyEntityOperation(this.getQueryId(), id, oldIndex, newIndex, queryEntityWraper);
				operation.setExtra(oldData);
			}
			else{
				operation = HAPEntityOperationFactory.createQueryRemoveEntityOperation(this.getQueryId(), id);
				if(scope!=HAPConstant.CONS_ENTITYOPERATION_SCOPE_UNDEFINED)  operation.setScope(scope);   
			}
			
		}
		else{
			//not there, need add
			operation = this.addQueryEntityOperation(queryEntityWraper, scope);
		}
		return operation;
	}
	
	
	/******************************************   Manipulate Containers (add/remove element)  *********************************************/
	public void addQueryEntityWrapers(List<HAPQueryEntityWraper> wrapers){
		for(HAPQueryEntityWraper wraper : wrapers){
			this.addQueryEntityWraper(wraper, -1);
		}
	}

	protected HAPQueryEntityWraper addEntityWraper(HAPEntityWraper wraper){
		HAPQueryEntityWraper queryEntityWraper = this.createQueryEntityWraper(wraper); 
		this.addQueryEntityWraper(queryEntityWraper);
		return queryEntityWraper;
	}
	
	protected int addQueryEntityWraper(HAPQueryEntityWraper wraper){
		int position = this.findPosition(wraper);
		if(this.m_segmentInfo.ifWithinSegment(position)){
			this.addQueryEntityWraper(wraper, position);
		}
		return position;
	}
	
	protected void addQueryEntityWraper(HAPQueryEntityWraper wraper, int position){
		this.m_entitys.put(wraper.getId(), wraper);
		if(position!=-1)		this.m_entityIdList.add(position, wraper.getId());    //valid position
		else  this.m_entityIdList.add(wraper.getId());
		
		for(HAPEntityID ID : wraper.getEntityIDs()){
			Set<String> queryEntitys = this.m_entityIDToQueryId.get(ID.toString());
			if(queryEntitys==null){
				queryEntitys = new HashSet<String>();
				this.m_entityIDToQueryId.put(ID.toString(), queryEntitys);
			}
			queryEntitys.add(wraper.getId());
		}
		this.m_segmentInfo.increase();
	}
	
	protected void removeQueryEntityById(String queryEntityId){
		HAPQueryEntityWraper queryEntity = (HAPQueryEntityWraper)this.m_entitys.remove(queryEntityId);
		if(queryEntity!=null){
			this.m_entityIdList.remove(queryEntityId);
			for(HAPEntityID ID : queryEntity.getEntityIDs()){
				Set<String> queryEntityIds = this.m_entityIDToQueryId.get(ID.toString());
				if(queryEntityIds!=null){
					queryEntityIds.remove(queryEntityIds);
					if(queryEntityIds.isEmpty()){
						this.m_entityIDToQueryId.remove(ID.toString());
					}
				}
			}
			this.m_segmentInfo.reduce();
		}
	}

	protected void removeQueryEntityWraper(HAPQueryEntityWraper wraper){
		this.removeQueryEntityById(wraper.getId().toString());
	}
	
	protected int findPosition(HAPQueryEntityWraper queryEntityWraper){
		return 0;
	}
	
	/******************************************   Basic  *********************************************/
	public HAPQueryDefinition getQueryDefinition(){return this.m_queryDefinition;}
	public String getQueryId(){return this.m_queryId;}
	
	public HAPQueryEntityWraper getQueryEntityWraper(String id){	return this.m_entitys.get(id);	}
	
	public List<HAPQueryEntityWraper> getEntitys(){
		List<HAPQueryEntityWraper> out = new ArrayList<HAPQueryEntityWraper>();
		for(String id : this.m_entityIdList){
			out.add(m_entitys.get(id));
		}
		return out;
	}

	public void setQueryManager(HAPQueryManager queryMan){this.m_queryMan = queryMan;}
	
	protected HAPEntityDataAccess getEntityDataAccess(){return this.m_queryMan.getDataAccess();}
	
	/******************************************   Clone  *********************************************/
	public HAPQueryComponent clone(HAPEntityDataAccess dataAccess){
		HAPQueryComponent out = new HAPQueryComponent(this.m_queryId, this.m_queryDefinition, this.m_queryParms, this.m_queryMan, this.m_dataTypeMan);

//		out.m_entityIDArray.addAll(this.m_entityIDArray);
//		
//		List<HAPQueryEntityWraper> queryWrapers = new ArrayList<HAPQueryEntityWraper>();
//		for(String key : this.m_entitys.keySet()){
//			queryWrapers.add(this.m_entitys.get(key).clone(dataAccess));
//		}

		for(String key : this.m_entitys.keySet()){
			out.addQueryEntityWraper(this.m_entitys.get(key).clone(dataAccess));
		}
		
		return out;
	}
	
	/******************************************   Serialization  *********************************************/
	@Override
	public String toStringValue(String format) {
		return "";
//		Map<String, String> jsonMap = new LinkedHashMap<String, String>();
//
//		if(this.m_query!=null)		jsonMap.put(HAPConstant.QUERY_ATTRIBUTE_QUERY, this.m_query.toStringValue(format));
//		
//		HAPDataTypeInfo dataTypeInfo = new HAPDataTypeInfo(HAPConstant.CATEGARY_CONTAINER, HAPConstant.DATATYPE_CONTAINER_QUERY);
//		jsonMap.put(HAPConstant.WRAPER_ATTRIBUTE_DATATYPE, dataTypeInfo.toStringValue(format));
//		
//		List<String> entityJson = new ArrayList<String>();
//		for(HAPQueryEntityWraper entityWraper : this.getEntitys()){
//			entityJson.add(entityWraper.toStringValue(HAPConstant.FORMAT_JSON));
//		}
//		jsonMap.put(HAPConstant.JSON_ATTRIBUTE_DATA_DATA, HAPJsonUtility.getArrayJson(entityJson.toArray(new String[0])));
//		
//		return HAPJsonUtility.getMapJson(jsonMap);
	}	
}
