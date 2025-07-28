package com.nosliw.core.data;

import com.nosliw.common.constant.HAPAttribute;
import com.nosliw.core.resource.imp.js.HAPResourceDataJSValue;
import com.nosliw.data.core.data.HAPDataTypeId;

public interface HAPResourceDataJSConverter extends HAPResourceDataJSValue{

	@HAPAttribute
	public static String DATATYPENAME = "dataTypeName";

	HAPDataTypeId getDataTypeName();
}
