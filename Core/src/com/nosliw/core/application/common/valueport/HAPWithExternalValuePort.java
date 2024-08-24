package com.nosliw.core.application.common.valueport;

import com.nosliw.common.constant.HAPAttribute;

public interface HAPWithExternalValuePort {

	@HAPAttribute
	public final static String EXTERNALVALUEPORT = "externalValuePort"; 
	
	HAPContainerValuePorts getExternalValuePorts();

}
