package com.nosliw.core.application.common.valueport;

import com.nosliw.common.constant.HAPAttribute;
import com.nosliw.common.constant.HAPEntityWithAttribute;

@HAPEntityWithAttribute
public interface HAPWithInternalValuePort extends HAPWithValuePort{

	@HAPAttribute
	public final static String INTERNALVALUEPORT = "internalValuePort"; 
	
	HAPContainerValuePorts getInternalValuePorts();

}
