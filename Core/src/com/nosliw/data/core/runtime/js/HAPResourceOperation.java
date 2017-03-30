package com.nosliw.data.core.runtime.js;

import com.nosliw.common.constant.HAPAttribute;
import com.nosliw.data.core.HAPDataTypeId;
import com.nosliw.data.core.runtime.HAPResourceData;

public interface HAPResourceOperation extends HAPResourceData{

	@HAPAttribute
	public static String SCRIPT = "script";
	
	@HAPAttribute
	public static String OPERATIONID = "operationId";

	@HAPAttribute
	public static String OPERATIONNAME = "operationName";
	
	@HAPAttribute
	public static String DATATYPENAME = "dataTypeName";
	
	String getScript();
	
	String getOperationId();
	
	String getOperationName();
	
	HAPDataTypeId getDataTypeName();
	
}
