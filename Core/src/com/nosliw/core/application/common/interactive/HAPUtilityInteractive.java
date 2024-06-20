package com.nosliw.core.application.common.interactive;

import com.nosliw.common.utils.HAPConstantShared;
import com.nosliw.common.utils.HAPUtilityNamingConversion;
import com.nosliw.core.application.common.valueport.HAPGroupValuePorts;
import com.nosliw.core.application.common.valueport.HAPValuePort;

public class HAPUtilityInteractive {

	public static HAPGroupValuePorts buildExternalInteractiveTaskValuePortGroup(HAPInteractiveTask interactive) {
		HAPGroupValuePorts out = new HAPGroupValuePorts();
		
		HAPValuePort requestValuePort = new HAPValuePortInteractiveRequest(interactive.getRequestParms(), HAPConstantShared.IO_DIRECTION_IN);
		requestValuePort.setName(HAPConstantShared.VALUEPORT_NAME_INTERACT_REQUEST);
		out.addValuePort(requestValuePort, true);
		
		for(Object key : interactive.getResults().keySet()) {
			String resultName = (String)key;
			HAPValuePort resultValuePort = new HAPValuePortInteractiveTaskResult(interactive.getResults().get(resultName), HAPConstantShared.IO_DIRECTION_OUT);
			resultName = HAPUtilityNamingConversion.cascadeComponents(HAPConstantShared.VALUEPORT_NAME_INTERACT_RESULT, resultName, HAPConstantShared.SEPERATOR_PREFIX);
			resultValuePort.setName(resultName);
			out.addValuePort(resultValuePort, false);
		}
		return out;
	}

}
