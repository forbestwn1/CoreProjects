package com.nosliw.data.core.runtime;

import com.nosliw.common.constant.HAPAttribute;
import com.nosliw.data.core.HAPDataTypeId;
import com.nosliw.data.core.runtime.js.HAPResourceDataJSValue;

public interface HAPResourceDataConverter extends HAPResourceDataJSValue{

	@HAPAttribute
	public static String DATATYPENAME = "dataTypeName";

	@HAPAttribute
	public static String CONVERTERTYPE = "converterType";
	
	HAPDataTypeId getDataTypeName();
	
	String getConverterType();
}
