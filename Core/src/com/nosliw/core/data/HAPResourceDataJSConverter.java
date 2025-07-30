package com.nosliw.core.data;

import com.nosliw.common.constant.HAPAttribute;
import com.nosliw.core.resource.imp.js.HAPResourceDataJSValue;

public interface HAPResourceDataJSConverter extends HAPResourceDataJSValue{

	@HAPAttribute
	public static String DATATYPENAME = "dataTypeName";

	HAPDataTypeId getDataTypeName();
}
