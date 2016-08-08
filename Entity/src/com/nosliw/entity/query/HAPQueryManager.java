package com.nosliw.entity.query;

import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;

import com.nosliw.data.HAPData;
import com.nosliw.data.HAPDataTypeManager;
import com.nosliw.entity.data.HAPEntityWraper;
import com.nosliw.entity.dataaccess.HAPEntityDataAccess;
import com.nosliw.entity.dataaccess.HAPOperationAllResult;

/*
 * query manager stay within each data access
 * 		run query
 * 		query component management
 * 		update query according to the entity change
 */
public abstract class HAPQueryManager {

	//all querys (queryId --- queryComponent)
	protected Map<String, HAPQueryComponent> m_querys;

	protected HAPEntityDataAccess m_dataAccess; 
	
	//store all the operations related with query
	protected HAPOperationAllResult m_results;
	
	public HAPQueryManager(HAPEntityDataAccess dataAccess){
		this.m_dataAccess = dataAccess;
		this.m_querys = new LinkedHashMap<String, HAPQueryComponent>();
	}
	
	/*
	 * new query
	 * isRequest  :
	 * 		true :  manage the query component
	 * 		false:  query component is just a return value
	 */
	abstract public HAPQueryComponent query(HAPQueryDefinition query, Map<String, HAPData> queryParms, HAPPageInfo pageInfo, boolean isRequest);
	/*
	 * existing query with different pageInfo
	 * isRequest  :
	 * 		true :  manage the query component
	 * 		false:  query component is just a return value
	 */
	abstract public HAPQueryComponent query(String queryId, HAPPageInfo pageInfo, boolean isRequest);
	
	public HAPQueryComponent addQueryComponent(HAPQueryComponent queryComponent){
		//change data access in query component to current data access
		queryComponent.setQueryManager(this);
		this.m_querys.put(queryComponent.getQueryId(), queryComponent);
		return queryComponent;
	}
	
	public void clearup(){
		this.m_dataAccess = null;
		this.m_results.clearup();
		Iterator<HAPQueryComponent> its = this.m_querys.values().iterator();
		while(its.hasNext()){
			its.next().clearup();
		}
		this.m_querys.clear();
	}
	
	public HAPEntityDataAccess getDataAccess(){	return this.m_dataAccess;}
	
	/*
	 * method related with boundary of operation result
	 */
	public void openOperationResult(){	this.m_results = new HAPOperationAllResult();}
	public void closeOperationResult(){ this.m_results.clearup(); }
	
	/*
	 * get all query ids
	 */
	public Set<String> getAllQuerys(){	return this.m_querys.keySet();	}
	
	/*
	 * get query component by id
	 */
	public HAPQueryComponent getQuery(String id){		return this.m_querys.get(id);	}
	
	/*
	 * remove query component by id
	 */
	public void removeQuery(String id){
		HAPQueryComponent component = this.m_querys.remove(id);
		component.clearup();
	}

	/*
	 * update query components for new entity
	 */
	public void newEntity(HAPEntityWraper entity){
		Iterator<HAPQueryComponent> its = this.m_querys.values().iterator();
		while(its.hasNext()){
			its.next().newEntity(entity);
		}
	}
	
	/*
	 * update query components for dead entity
	 */
	public void deleteEntity(HAPEntityWraper entity){
		Iterator<HAPQueryComponent> its = this.m_querys.values().iterator();
		while(its.hasNext()){
			its.next().deleteEntity(entity.getID());
		}
	}

	/*
	 * update query components for changed entity
	 */
	public void changeEntity(HAPEntityWraper entity){
		Iterator<HAPQueryComponent> its = this.m_querys.values().iterator();
		while(its.hasNext()){
			its.next().changeEntity(entity);
		}
	}
	
	protected HAPQueryManager getUnderQueryManager(){return this.m_dataAccess.getUnderDataAccess().getQueryManager();}
	protected HAPQueryDefinitionManager getQueryDefinitionManager(){ return this.m_dataAccess.getDataContext().getQueryDefinitionManager();}
	protected HAPDataTypeManager getDataTypeManager(){ return this.m_dataAccess.getDataContext().getDataTypeManager(); }
	
	protected String createQueryId(){return System.currentTimeMillis()+"";	}
}
