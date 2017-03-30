package com.nosliw.data.core.runtime.js;

import com.nosliw.common.constant.HAPAttribute;
import com.nosliw.data.core.runtime.HAPResourceData;

public interface HAPResourceHelper extends HAPResourceData{

	@HAPAttribute
	public static String SCRIPT = "script";
	
	String getScript();
	
}
