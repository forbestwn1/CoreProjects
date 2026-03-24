package com.nosliw.core.xxx.application1;

import com.nosliw.common.constant.HAPAttribute;
import com.nosliw.common.constant.HAPEntityWithAttribute;

@HAPEntityWithAttribute
public interface HAPWithValueContext {

	@HAPAttribute
	public static final String VALUECONTEXT = "valueContext";

	HAPValueContext getValueContext();
	
}
