package com.nosliw.core.application.common.valueport;

import com.nosliw.common.constant.HAPAttribute;

public interface HAPWithInternalValuePort {

	@HAPAttribute
	public final static String INTERNALVALUEPORT = "internalValuePort"; 
	
	HAPContainerValuePorts getInternalValuePorts();

}
