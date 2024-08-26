package com.nosliw.core.application.common.valueport;

import com.nosliw.common.constant.HAPAttribute;
import com.nosliw.common.constant.HAPEntityWithAttribute;

@HAPEntityWithAttribute
public interface HAPWithInternalValuePortGroup {

	@HAPAttribute
	public static String VALUEPORTGROUP = "valuePortGroup";
	
	HAPGroupValuePorts getInternalValuePortGroup();

}
