package com.nosliw.data.core.runtime.js.resource;

import com.nosliw.common.constant.HAPAttribute;
import com.nosliw.data.core.data.HAPDataTypeId;
import com.nosliw.data.core.runtime.js.HAPResourceDataJSValue;

public interface HAPResourceDataJSOperation extends HAPResourceDataJSValue{

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
