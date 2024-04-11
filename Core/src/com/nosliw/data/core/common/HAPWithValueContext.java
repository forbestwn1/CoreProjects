package com.nosliw.data.core.common;

import com.nosliw.common.constant.HAPAttribute;

public interface HAPWithValueContext {

	@HAPAttribute
	public static String VALUECONTEXT = "valueContext";

	HAPManualBrickValueContext getValueContextEntity();

	void setValueContextEntity(HAPManualBrickValueContext valueContext);
	
}
