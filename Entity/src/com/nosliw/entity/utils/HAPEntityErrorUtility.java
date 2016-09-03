package com.nosliw.entity.utils;

import com.nosliw.common.exception.HAPServiceData;
import com.nosliw.common.utils.HAPConstant;
import com.nosliw.entity.dataaccess.HAPOperationAllResult;
import com.nosliw.entity.operation.HAPEntityOperation;
import com.nosliw.entity.operation.HAPEntityOperationInfo;

public class HAPEntityErrorUtility {

	/*
	 * utility method to create error service data when auto commit return error
	 */
	public static HAPServiceData createEntityOperationAutoCommitError(HAPOperationAllResult results, String message, Exception ex){
		HAPServiceData out = HAPServiceData.createServiceData(HAPConstant.ERRORCODE_ENTITYOPERATION_AUTOCOMMIT, results, message);
		out.setException(ex);
		return out;
	}

	public static HAPServiceData createEntityOperationInvalidTransaction(HAPEntityOperation op, String message, Exception ex){
		HAPServiceData out = HAPServiceData.createServiceData(HAPConstant.ERRORCODE_ENTITYOPERATION_INVALIDTRANSACTION, op, message);
		out.setException(ex);
		return out;
	}
	
	/*
	 * utility method to create error service data when auto commit return error
	 */
	public static HAPServiceData createEntityOperationInvalidScopeError(HAPEntityOperationInfo operationInfo, String message, Exception ex){
		HAPServiceData out = HAPServiceData.createServiceData(HAPConstant.ERRORCODE_ENTITYOPERATION_INVALIDSCOPE, operationInfo, message);
		out.setException(ex);
		return out;
	}
	
	
}
