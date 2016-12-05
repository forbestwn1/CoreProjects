package com.nosliw.data;

import com.nosliw.common.constant.HAPAttribute;
import com.nosliw.common.constant.HAPEntityWithAttribute;

@HAPEntityWithAttribute(baseName="DATATYPEOPERATION")
public interface HAPDataTypeOperation {

	@HAPAttribute
	public static String OPERATIONINFO = "operationInfo";
	
	@HAPAttribute
	public static String BASEDATATYPE = "baseDataType";
	
	HAPOperationInfo getOperationInfo();
	
	HAPDataTypePictureNode getBaseDataType();
	
}
