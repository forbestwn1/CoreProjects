package com.nosliw.data.core.component;

import com.nosliw.common.constant.HAPAttribute;

public interface HAPWithNameMapping {

	@HAPAttribute
	public static String NAMEMAPPING = "nameMapping";
	
	HAPNameMapping getNameMapping();

}
