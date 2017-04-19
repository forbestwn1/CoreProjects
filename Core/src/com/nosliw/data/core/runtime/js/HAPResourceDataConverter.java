package com.nosliw.data.core.runtime.js;

import com.nosliw.common.constant.HAPAttribute;
import com.nosliw.data.core.HAPDataTypeId;

public interface HAPResourceDataConverter extends HAPResourceDataJSValue{

	@HAPAttribute
	public static String DATATYPENAME = "dataTypeName";

	@HAPAttribute
	public static String CONVERTERTYPE = "converterType";
	
	HAPDataTypeId getDataTypeName();
	
	String getConverterType();
}
