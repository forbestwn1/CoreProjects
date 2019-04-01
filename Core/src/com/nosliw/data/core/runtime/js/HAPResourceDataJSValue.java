package com.nosliw.data.core.runtime.js;

import com.nosliw.common.constant.HAPAttribute;
import com.nosliw.data.core.resource.HAPResourceData;

public interface HAPResourceDataJSValue extends HAPResourceData{

	@HAPAttribute
	public static String VALUE = "value";
	
	String getValue();
}
