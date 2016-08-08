package com.nosliw.entity.utils;

import com.nosliw.common.exception.HAPServiceData;
import com.nosliw.common.utils.HAPConstant;
import com.nosliw.entity.data.HAPEntityID;
import com.nosliw.entity.operation.HAPEntityOperation;

public class HAPTransactionUtility {

	/*
	 * set transaction id data to service data
	 */
//	public static void setServiceDataTransactionId(HAPServiceData serviceData, long transactionId){
//		serviceData.setMetaData(HAPConstant.CONS_SERVICEDATA_METADATA_TRANSACTIONID, String.valueOf(transactionId));
//	}
//	public static void setServiceDataTransactionId(HAPServiceData serviceData, String transactionId){
//		serviceData.setMetaData(HAPConstant.CONS_SERVICEDATA_METADATA_TRANSACTIONID, transactionId);
//	}
	
	
	private static int[][] m_scopes = {
		{HAPConstant.CONS_ENTITYOPERATION_SCOPE_OPERATION, HAPConstant.CONS_ENTITYOPERATION_SCOPE_GLOBAL, HAPConstant.CONS_ENTITYOPERATION_SCOPE_OPERATION},
		{-1, HAPConstant.CONS_ENTITYOPERATION_SCOPE_ENTITY, HAPConstant.CONS_ENTITYOPERATION_SCOPE_ENTITY},
		{-1, -1, -1}
	};

	public static int getCurrentOperationScope(int accessScope, int parentScope){
		return m_scopes[accessScope][parentScope];
	}
	
	/*
	 * get transaction id data from service data
	 */
	public static long getServiceDataTransactionId(HAPServiceData serviceData){
		String trasIdStr = serviceData.getMetaData(HAPConstant.CONS_SERVICEDATA_METADATA_TRANSACTIONID);
		return Long.parseLong(trasIdStr);
	}

	static public boolean isNormalEntityOperation(HAPEntityOperation operation){
		boolean out = operation.equals(HAPEntityOperation.ENTITYOPERATION_ENTITY_DELETE) || 
				operation.equals(HAPEntityOperation.ENTITYOPERATION_ENTITY_NEW) ||
				operation.equals(HAPEntityOperation.ENTITYOPERATION_ATTR_ELEMENT_NEW);
		return !out;
	}

	static public boolean isEntityBelongsToUserContext(HAPEntityID ID){
//	static public boolean isEntityBelongsToUserContext(HAPEntityID ID, HAPUserContext userContext){
//		if(userContext.getId().equals(ID.getUserContext())){
//			return true;
//		}
//		else return false;
		return true;
	}
	
}
