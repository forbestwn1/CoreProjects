package com.nosliw.app.service;

import java.util.Map;

import com.nosliw.common.exception.HAPServiceData;
import com.nosliw.data.HAPData;
import com.nosliw.entity.data.HAPEntityID;
import com.nosliw.entity.dataaccess.HAPEntityRequestInfo;
import com.nosliw.entity.operation.HAPEntityOperationInfo;
import com.nosliw.entity.query.HAPPageInfo;
import com.nosliw.entity.query.HAPQueryDefinition;

public interface HAPEntityService {

	//********************************* transaction  ************************************ 
	/*
	 * return if success, return transaction object
	 *        if fail, return error
	 */
	public HAPServiceData startTransaction();
	/*
	 * return value depend on return value of commit of different type of transaction 
	 */
	public HAPServiceData commit();
	/*
	 * return value: the updated entity
	 */
	public HAPServiceData rollBack();
	
	//********************************* Query  ************************************ 
	public HAPServiceData query(HAPQueryDefinition query, Map<String, HAPData> queryParms, HAPPageInfo pageInfo);
	public HAPServiceData remvoeQuery(String queryId);
	public HAPServiceData getEntityWrapers(HAPEntityRequestInfo requestInfo);

	public HAPServiceData getAttributeOptionsData(HAPEntityID ID, String attrPath);
	
	
	//********************************* Entity Operation  ************************************ 
	/*
	 * return : if the operation success
	 * 			if one of them fail 
	 * 				failure message
	 * 				stop do following operation
	 * 			a list of entity needed to update 
	 */
	
	
	/*
	 * single operation
	 */
	public HAPServiceData operate(HAPEntityOperationInfo operation, HAPRequestInfo reqInfo);
	

	/*
	 * batch operations at the same time
	 */
	public HAPServiceData operate(HAPEntityOperationInfo[] operations, HAPRequestInfo reqInfo);
	
}
