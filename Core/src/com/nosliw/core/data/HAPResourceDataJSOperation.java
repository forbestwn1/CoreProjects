package com.nosliw.core.data;

import com.nosliw.common.constant.HAPAttribute;
import com.nosliw.core.resource.imp.js.HAPResourceDataJSValue;

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
