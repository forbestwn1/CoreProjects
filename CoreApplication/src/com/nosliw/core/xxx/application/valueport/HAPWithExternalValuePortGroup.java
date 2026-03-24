package com.nosliw.core.xxx.application.valueport;

import com.nosliw.common.constant.HAPAttribute;
import com.nosliw.common.constant.HAPEntityWithAttribute;
import com.nosliw.core.application.valueport.HAPGroupValuePorts;

@HAPEntityWithAttribute
public interface HAPWithExternalValuePortGroup {

	@HAPAttribute
	public static String VALUEPORTGROUP = "valuePortGroup";
	
	HAPGroupValuePorts getExternalValuePortGroup();

}
