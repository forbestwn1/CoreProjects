package com.nosliw.core.application;

import com.nosliw.common.constant.HAPAttribute;
import com.nosliw.common.constant.HAPEntityWithAttribute;
import com.nosliw.core.application.valuecontext.HAPValueContext;

@HAPEntityWithAttribute
public interface HAPWithValueContext {

	@HAPAttribute
	public static final String VALUECONTEXT = "valueContext";

	HAPValueContext getValueContext();
	
}
