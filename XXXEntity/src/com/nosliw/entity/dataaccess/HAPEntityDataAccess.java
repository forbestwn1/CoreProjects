package com.nosliw.entity.dataaccess;

import java.util.List;
import java.util.Map;
import java.util.Set;

import com.nosliw.common.exception.HAPServiceData;
import com.nosliw.common.serialization.HAPSerializable;
import com.nosliw.data.HAPData;
import com.nosliw.entity.data.HAPEntityID;
import com.nosliw.entity.data.HAPEntityWraper;
import com.nosliw.entity.data.HAPReferenceInfoAbsolute;
import com.nosliw.entity.event.HAPEventListener;
import com.nosliw.entity.operation.HAPEntityOperationInfo;
import com.nosliw.entity.query.HAPPageInfo;
import com.nosliw.entity.query.HAPQueryComponent;
import com.nosliw.entity.query.HAPQueryDefinition;
import com.nosliw.entity.query.HAPQueryManager;

/*
 * 
 */
public interface HAPEntityDataAccess extends HAPEventListener, HAPSerializable{
	
	public void init();
	public void destroy();
	
	public HAPQueryManager getQueryManager();
	public HAPTransitionEntityManager getTransitionEntityManager();
	
	public HAPEntityDataAccess getUnderDataAccess();
	
	//query method
	/*
	 * if success, return entity based on current transaction
	 */
	public HAPServiceData useEntityByID(HAPEntityID ID);
	public HAPServiceData readEntityByID(HAPEntityID ID);
	public HAPServiceData getEntityByID(HAPEntityID ID, boolean ifKeep);
	
	public HAPReferenceManager getReferenceManager();
	
//	public HAPServiceData getEntityByAttributeValue(String group, String attribute, String value);
	/*
	 * query
	 * 	 	query  		: 	query information
	 * 		queryParms 	: 	query parameters
	 * 		pageInfo	:	page info for query result
	 */
	public HAPQueryComponent queryRequest(HAPQueryDefinition query, Map<String, HAPData> queryParms, HAPPageInfo pageInfo);
	/*
	 * get another page for query
	 */
	public HAPQueryComponent queryRequest(String queryId, HAPPageInfo pageInfo);
	/*
	 * update query according to operation result after operation
	 */
	public void updateQueryByResult();
	public void removeQuery(String queryId);
	public Set<String> getAllQuerys();
	public HAPQueryComponent getQueryComponent(String id);

	
//	public List<HAPEntityWraper> queryEntityWrapers(HAPQueryDefinition query);
//	public HAPQueryComponent query(HAPQueryDefinition query, Map<String, HAPData> queryParms);
	
	public Set<HAPEntityWraper> getAllTransitEntitys();
	public Set<HAPEntityWraper> getTransitEntitysByType(String type);
	public Set<HAPEntityWraper> getTransitEntitysByStatus(int status);

	
	/*
	 * is another transaction for operation is allowed on top of current transaction
	 * if operation is null, means normal transaction
	 */
//	public HAPServiceData isValidOperationTransaction(HAPEntityOperationInfo operation);
	/*
	 * do operation 
	 */
	public HAPServiceData operate(HAPEntityOperationInfo operation);
	public HAPServiceData isValidOperation(HAPEntityOperationInfo operation);
	
	
	public void clearReferenceToEntity(HAPEntityID ID);
	public void breakEntityReference(HAPReferenceInfoAbsolute referencePath, HAPEntityID ID);
	public void buildEntityReference(HAPReferenceInfoAbsolute referencePath, HAPEntityID ID);
	
	
	public void openOperationResult();
	public void addOperationResult(HAPEntityOperationInfo resutl);
	public void closeOperationResult();
	public HAPOperationAllResult getOperationResult();
	
	public int getOperationScope();
	public void setOperationScope(int scope);
	
	/*
	 * commit the change to the lower level (for entity loader, lower level means db) 
	 */
	public HAPOperationAllResult commit();
	
	public HAPServiceData preCommit();
	public HAPServiceData postCommit();

	/*
	 * this method should be called by upper level,
	 * it does the merge of change of upper level to this level during commitment of 
	 */
	public void submitOperation(HAPEntityOperationInfo operation);
	
	public HAPDataContext getDataContext();
}
