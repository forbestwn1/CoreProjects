package com.nosliw.data.core.runtime.js.resource;

import com.nosliw.common.constant.HAPAttribute;
import com.nosliw.data.core.HAPDataTypeId;
import com.nosliw.data.core.runtime.js.HAPResourceDataJSValue;

public interface HAPResourceDataJSConverter extends HAPResourceDataJSValue{

	@HAPAttribute
	public static String DATATYPENAME = "dataTypeName";

	HAPDataTypeId getDataTypeName();
}
