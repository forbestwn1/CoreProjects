package com.nosliw.data.core.runtime.js;

import com.nosliw.common.constant.HAPAttribute;
import com.nosliw.data.core.HAPDataTypeId;

public interface HAPResourceDataOperation extends HAPResourceDataScript{

	@HAPAttribute
	public static String OPERATIONID = "operationId";

	@HAPAttribute
	public static String OPERATIONNAME = "operationName";
	
	@HAPAttribute
	public static String DATATYPENAME = "dataTypeName";
	
	String getOperationId();
	
	String getOperationName();
	
	HAPDataTypeId getDataTypeName();
	
}
