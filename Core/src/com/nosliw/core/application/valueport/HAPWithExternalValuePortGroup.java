package com.nosliw.core.application.valueport;

import com.nosliw.common.constant.HAPAttribute;
import com.nosliw.common.constant.HAPEntityWithAttribute;

@HAPEntityWithAttribute
public interface HAPWithExternalValuePortGroup {

	@HAPAttribute
	public static String VALUEPORTGROUP = "valuePortGroup";
	
	HAPGroupValuePorts getExternalValuePortGroup();

}
