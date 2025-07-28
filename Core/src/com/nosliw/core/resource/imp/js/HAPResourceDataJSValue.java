package com.nosliw.core.resource.imp.js;

import com.nosliw.common.constant.HAPAttribute;
import com.nosliw.common.constant.HAPEntityWithAttribute;
import com.nosliw.data.core.resource.HAPResourceData;

@HAPEntityWithAttribute
public interface HAPResourceDataJSValue extends HAPResourceData{

	@HAPAttribute
	public static String VALUE = "value";
	
	String getValue();
}
