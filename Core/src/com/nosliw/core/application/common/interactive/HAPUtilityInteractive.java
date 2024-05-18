package com.nosliw.core.application.common.interactive;

import com.nosliw.core.application.common.valueport.HAPGroupValuePorts;

public class HAPUtilityInteractive {

	public static HAPGroupValuePorts buildInteractiveValuePortGroup(HAPInteractive interactive) {

		HAPGroupValuePorts out = new HAPGroupValuePorts();
		
		out.addValuePort(new HAPValuePortInteractiveRequest(interactive), false);
		
		for(Object key : interactive.getResults().keySet()) {
			out.addValuePort(new HAPValuePortInteractiveResult(interactive, (String)key), false);
		}
		return out;
	}
	
	
}

