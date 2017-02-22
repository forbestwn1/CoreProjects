package com.nosliw.data;

import com.nosliw.common.constant.HAPAttribute;
import com.nosliw.common.constant.HAPEntityWithAttribute;

/**
 * Operation related with source data type
 * It contains two information:
 * 		operation itself including operation name, parms, output
 * 		target data type with which this operation is defined 
 */
@HAPEntityWithAttribute(baseName="DATATYPEOPERATION")
public interface HAPDataTypeOperation {

	@HAPAttribute
	public static String OPERATIONINFO = "operationInfo";
	
	@HAPAttribute
	public static String TARGETDATATYPE = "targetDataType";
	
	HAPOperation getOperationInfo();
	
	HAPRelationship getTargetDataType();
	
}
