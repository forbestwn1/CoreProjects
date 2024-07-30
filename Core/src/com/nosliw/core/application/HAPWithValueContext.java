package com.nosliw.core.application;

import com.nosliw.common.constant.HAPAttribute;
import com.nosliw.common.constant.HAPEntityWithAttribute;

@HAPEntityWithAttribute
public interface HAPWithValueContext {

	@HAPAttribute
	public static final String VALUECONTEXT = "valueContext";

	HAPValueContext getValueContext();
	
}
