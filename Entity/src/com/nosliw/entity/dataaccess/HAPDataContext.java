package com.nosliw.entity.dataaccess;

import java.util.Collection;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Stack;

import com.nosliw.common.configure.HAPConfigure;
import com.nosliw.common.exception.HAPServiceData;
import com.nosliw.common.serialization.HAPStringable;
import com.nosliw.common.utils.HAPConstant;
import com.nosliw.common.utils.HAPJsonUtility;
import com.nosliw.data.HAPData;
import com.nosliw.data.HAPDataTypeManager;
import com.nosliw.entity.data.HAPDataWraper;
import com.nosliw.entity.data.HAPEntityContainerAttributeWraper;
import com.nosliw.entity.data.HAPEntityID;
import com.nosliw.entity.data.HAPEntityWraper;
import com.nosliw.entity.data.HAPReferenceWraper;
import com.nosliw.entity.definition.HAPEntityDefinitionManager;
import com.nosliw.entity.operation.HAPEntityOperation;
import com.nosliw.entity.operation.HAPEntityOperationInfo;
import com.nosliw.entity.options.HAPOptionsDefinitionManager;
import com.nosliw.entity.query.HAPPageInfo;
import com.nosliw.entity.query.HAPQueryComponent;
import com.nosliw.entity.query.HAPQueryDefinition;
import com.nosliw.entity.query.HAPQueryDefinitionManager;
import com.nosliw.entity.transaction.HAPTransaction;
import com.nosliw.entity.transaction.HAPTransactionDeleteEntity;
import com.nosliw.entity.transaction.HAPTransactionEntityMultiOperates;
import com.nosliw.entity.transaction.HAPTransactionEntityOperate;
import com.nosliw.entity.transaction.HAPTransactionNewElement;
import com.nosliw.entity.transaction.HAPTransactionNewEntity;
import com.nosliw.entity.transaction.HAPTransactionNormal;
import com.nosliw.entity.utils.HAPAttributeConstant;
import com.nosliw.entity.utils.HAPEntityDataTypeUtility;
import com.nosliw.entity.utils.HAPEntityErrorUtility;

/*
 * entity manager class that handle all type of entity managment task
 * it is responding to one client request, store all the state related with that client
 * 		entity query
 * 		entity operation
 * 		transaction
 * all the entity operator task is done by entity loader
 * the entity is grouped
 * so the identity for entity is categary + id
 */

public class HAPDataContext implements HAPStringable{
	//information related with user who are operating on this user env
	private HAPDataContextInfo m_dataContextInfo;
	//data access component for database access
	private HAPEntityPersistent m_persistant = null;
	//data access component stack for transaction
	private Stack<HAPTransaction> m_transactions;

	private HAPConfigure m_configure;
	private HAPDataTypeManager m_dataTypeMan; 
	private HAPEntityDefinitionManager m_entityDefMan; 
	private HAPQueryDefinitionManager m_queryDefMan;
	private HAPOptionsDefinitionManager m_optionsDefMan;
	
	public HAPDataContext(HAPDataContextInfo dataContextInfo,
						HAPEntityPersistent persistant, 
						HAPConfigure configure,
						HAPDataTypeManager dataTypeMan, 
						HAPEntityDefinitionManager entityDefMan, 
						HAPQueryDefinitionManager queryDefMan,
						HAPOptionsDefinitionManager optionsDefMan){
		this.m_dataContextInfo = dataContextInfo;
		this.m_persistant = persistant;
		this.m_transactions = new Stack<HAPTransaction>();
		
		this.m_configure = configure;
		this.m_dataTypeMan = dataTypeMan;
		this.m_entityDefMan = entityDefMan;
		this.m_queryDefMan = queryDefMan;
		this.m_optionsDefMan = optionsDefMan;
	}
	
	//*********************  Query
	/*
	 * query request
	 */
	public HAPQueryComponent queryRequest(HAPQueryDefinition query, Map<String, HAPData> queryParms, HAPPageInfo pageInfo){
		HAPEntityDataAccess dataAccess = this.getCurrentDataAccess();
		return dataAccess.queryRequest(query, queryParms, pageInfo);
	}
	
	/*
	 * remove query component from entity manager
	 */
	public void removeQueryRequest(String queryId){
		HAPEntityDataAccess dataAccess = this.getCurrentDataAccess();
		dataAccess.removeQuery(queryId);
	}

	/*
	 * update query content based on data operation result
	 */
	private void updateQueryByResult(){
		HAPEntityDataAccess dataAccess = this.getCurrentDataAccess();
		dataAccess.updateQueryByResult();
	}
	
	//*********************  Entity
	/*
	 * process request : get entitys
	 */
	public Map<String, HAPServiceData> getEntitysRequest(HAPEntityRequestInfo request){
		//whether load referenced entity
		if(request.ifLoadRelated()){
			return this.getEntitysAndRelated(request.getEntityIDs(), request, null);
		}
		else{
			return this.getEntitys(request.getEntityIDs(), request);
		}
	}

	/*
	 * get all entitys according to ID
	 * return map ( id key   -----   serviceData: success data; fail reason to fail )
	 */
	private Map<String, HAPServiceData> getEntitys(Collection<HAPEntityID> IDs, HAPEntityRequestInfo request){
		HAPEntityDataAccess dataAccess = this.getCurrentDataAccess();
		Map<String, HAPServiceData> out = new LinkedHashMap<String, HAPServiceData>();
		
		for(HAPEntityID ID : IDs){
			HAPServiceData dataService = dataAccess.getEntityByID(ID, request.ifKeepEntity());
			out.put(ID.toString(), dataService);
		}
		return out;
	}
	
	/*
	 * get entitys by ID and their referenced entitys
	 * 		existEntityIDs : retrieved ID, so that do not process them again
	 */
	private Map<String, HAPServiceData> getEntitysAndRelated(Collection<HAPEntityID> IDs, HAPEntityRequestInfo request, Set<HAPEntityID> existEntityIDs){
		
		if(existEntityIDs==null)  existEntityIDs = new HashSet<HAPEntityID>();

		HAPEntityDataAccess dataAccess = this.getCurrentDataAccess();
		Map<String, HAPServiceData> out = new LinkedHashMap<String, HAPServiceData>();
		for(HAPEntityID ID : IDs){
			//only process ID not in existEntityIDs
			if(!existEntityIDs.contains(ID)){
				//get entity by ID
				HAPServiceData dataService = dataAccess.getEntityByID(ID, request.ifKeepEntity());
				out.put(ID.toString(), dataService);
				//add to existing IDs
				existEntityIDs.add(ID);
				if(dataService.isSuccess()){
					HAPEntityWraper entityWraper = (HAPEntityWraper)dataService.getData();

					//get referenced entity
					Set<HAPEntityID> childRefEntityIDs = HAPEntityDataTypeUtility.getAllChildReferenceEntityID(entityWraper);
					Map<String, HAPServiceData> childRefEntitys = getEntitysAndRelated(childRefEntityIDs, request, existEntityIDs);
					for(String e : childRefEntitys.keySet())	out.put(e, childRefEntitys.get(e));
				}
			}
		}
		return out;
	}

	
	//*********************  Transaction
	/*
	 * start general transaction
	 */
	public HAPServiceData startTransactionRequest(){
		HAPEntityOperation op = HAPEntityOperation.ENTITYOPERATION_TRANSACTION_START;
		HAPServiceData serviceData = this.isValidOperation(op); 
		if(serviceData.isFail())  return serviceData;
		
		this.startTransaction(op);
		return HAPServiceData.createSuccessData(new HAPOperationAllResult());
	}
	
	/*
	 * commit opened transaction
	 */
	public HAPServiceData commitRequest(){
		HAPServiceData out = HAPServiceData.createFailureData();
		HAPTransaction trans = this.getCurrentTransaction(); 
		if(trans!=null){
			//preprocess, if success, then do real commitment
			out = trans.preCommit();
			if(out.isSuccess())	{
				HAPOperationAllResult results = trans.commit();
				//try to find new operations created because of commit, as only those new operation need to updated on client side  
				List<HAPEntityOperationInfo> operations = trans.getFullOperations();
				//comment out remove duplicate operations
				//    duplicate opeation will not cause issue on client side
				//    we don't have unique id for operation in order to remove duplicated operation
//				results.removeResult(operations);
				this.removeTopTransaction();
				out.setData(results);
			}
			trans.postCommit();
		}
		return out;
	}

	/*
	 * rollback opened transaction
	 */
	public HAPServiceData rollBackRequest(){
		HAPServiceData out = HAPServiceData.createFailureData();
		HAPTransaction trans = this.getCurrentTransaction(); 
		if(trans!=null){
			//get operation results when rollback
			HAPOperationAllResult result = trans.getRollBackResult();
			//delete transaction
			this.removeTopTransaction();
			out = HAPServiceData.createSuccessData(result);
		}
		return out;
	}
	
	/*
	 * method called right after some operation
	 * this operation should already start a transaction
	 */
	private HAPServiceData autoCommit(){
		HAPServiceData out = HAPServiceData.createFailureData();
		HAPTransaction trans = this.getCurrentTransaction();
		if(trans!=null){
			out = trans.preCommit();
			if(out.isSuccess())	{
				HAPOperationAllResult result = trans.commit();
				this.removeTopTransaction();
				out = HAPServiceData.createSuccessData(result);
			}
			trans.postCommit();
		}
		return out;
	}

	/*
	 * method called right after some operation
	 * this operation should already start a transaction
	 * if fail, just remove the transaction
	 */
	private HAPServiceData autoRollBack(){
		HAPServiceData out = HAPServiceData.createFailureData();
		HAPTransaction trans = this.getCurrentTransaction();
		if(trans!=null){
			this.removeTopTransaction();
			out = HAPServiceData.createSuccessData(new HAPOperationAllResult());
		}
		return out;
	}
	
	
	/*
	 * start a new trasaction according to operation info provided
	 */
	private HAPTransaction startTransaction(HAPEntityOperation op){
		HAPEntityDataAccess underMe = this.getCurrentDataAccess();

		HAPTransaction trans = null;
		switch(op){
		case ENTITYOPERATION_ENTITY_NEW:
			trans = new HAPTransactionNewEntity(this.getConfigure(), underMe, this);
			break;
		case ENTITYOPERATION_ENTITY_DELETE:
			trans = new HAPTransactionDeleteEntity(this.getConfigure(), underMe, this);
			break;
		case ENTITYOPERATION_TRANSACTION_START:
			trans = new HAPTransactionNormal(this.getConfigure(), underMe, this);
			break;
		case ENTITYOPERATION_ENTITY_OPERATIONS:
			trans = new HAPTransactionEntityMultiOperates(this.getConfigure(), underMe, this);
			break;
		case ENTITYOPERATION_ATTR_ELEMENT_NEW:
			trans = new HAPTransactionNewElement(this.getConfigure(), underMe, this);
			break;
		case ENTITYOPERATION_ATTR_ATOM_SET:
		case ENTITYOPERATION_ATTR_CRITICAL_SET:
		case ENTITYOPERATION_ATTR_ELEMENT_DELETE:
		case ENTITYOPERATION_ATTR_REFERENCE_SET:
//		case ENTITYOPERATION_ATTR_REFERENCE_CLEAR:
			trans = new HAPTransactionEntityOperate(this.getConfigure(), underMe, this);
			break;
		}
		//add transaction object to transaction stack
		this.m_transactions.add(trans);
		//init transaction
		trans.init();
		return trans;
	}
	
	/*
	 * check if it is valid operation based on current data access stack
	 */
	private HAPServiceData isValidOperation(HAPEntityOperation op){
		HAPEntityDataAccess dataAccess = this.getCurrentDataAccess();
//		this.getCurrentDataAccess().isValidOperationTransaction(null);
		
//		return HAPEntityErrorUtility.createEntityOperationInvalidTransaction(op, "", null);
		return HAPServiceData.createSuccessData();
	}

	//*********************  Operation
	/*
	 * process entity operation
	 */
	public HAPServiceData operateRequest(HAPEntityOperationInfo operation){
		//it is the request, so it is directly from client side, therefore, this operation is root operation
		operation.setIsRootOperation(true);

		//check if this operation is valid operation based on current data access
		HAPServiceData serviceData = this.isValidOperation(operation.getOperation());
		if(serviceData.isFail())  return serviceData;

		//create a new transaction for this operation
		HAPTransaction transaction = this.startTransaction(operation.getOperation());
		//init operation results
		transaction.openOperationResult();
		//if success, return all the change made by this operation
		serviceData = transaction.operate(operation);
		
		boolean ifUpdateQuery = false;

		if(serviceData.isFail()){
			//operation failed, just rollback : return erro code without any operation
			this.autoRollBack();
			serviceData.setData(new HAPOperationAllResult());
		}
		else{
			//operation success
			//get operation result
			HAPOperationAllResult results = transaction.getOperationResult();
			serviceData.setData(results);
			
			if(operation.isAutoCommit()){
				//for operation require auto commit, 
				HAPServiceData commitSData = this.autoCommit();
				if(commitSData.isFail()){
					//autocommit fail: return error code with operation result
					serviceData = HAPEntityErrorUtility.createEntityOperationAutoCommitError(results, commitSData.getMessage(), null); 
				}
				else{
					//autocommit success: retur success data
					serviceData = commitSData;
					ifUpdateQuery = true;
				}
			}
		}
		if(ifUpdateQuery)	this.updateQueryByResult();
		
		return serviceData;
	}

	/*
	 * get currnet data access object
	 */
	private HAPEntityDataAccess getCurrentDataAccess(){
		HAPEntityDataAccess out = this.m_persistant;
		if(!this.m_transactions.isEmpty()){
			out = this.m_transactions.lastElement();
		}
		return out;
	}
	
	/*
	 * get current transaction in transaction stack
	 * return null if no transaction exist
	 */
	private HAPTransaction getCurrentTransaction(){
		HAPTransaction out = null;
		if(!this.m_transactions.isEmpty()){
			out = this.m_transactions.lastElement();
		}
		return out;
	}

	/*
	 * remove the top transaction
	 */
	private void removeTopTransaction(){
		if(!this.m_transactions.isEmpty()){
			this.getCurrentTransaction().destroy();
			this.m_transactions.pop();
		}
	}

	
	public HAPDataTypeManager getDataTypeManager(){return this.m_dataTypeMan;}
	public HAPEntityDefinitionManager getEntityDefinitionManager(){return this.m_entityDefMan;}
	public HAPOptionsDefinitionManager getOptionsManager(){return this.m_optionsDefMan;}
	public HAPQueryDefinitionManager getQueryDefinitionManager(){return this.m_queryDefMan;}
	protected HAPConfigure getConfigure(){ return this.m_configure; }
	public HAPDataContextInfo getDataContextInfo(){ return this.m_dataContextInfo; }
	
	@Override
	public String toStringValue(String format){
		Map<String, String> jsonMap = new LinkedHashMap<String, String>();
		
		jsonMap.put(HAPAttributeConstant.ENTITYMANAGER_PERSISTANT, this.m_persistant.toStringValue(format));
		jsonMap.put(HAPAttributeConstant.ENTITYMANAGER_TRANSACTIONS, HAPJsonUtility.getListObjectJson(this.m_transactions));
		
		return HAPJsonUtility.getMapJson(jsonMap);
	}
	
	public String toString(){
		return HAPJsonUtility.formatJson(this.toStringValue(HAPConstant.SERIALIZATION_JSON));
	}
}
