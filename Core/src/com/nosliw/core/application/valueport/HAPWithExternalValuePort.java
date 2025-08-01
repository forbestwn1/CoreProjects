package com.nosliw.core.application.valueport;

import com.nosliw.common.constant.HAPAttribute;
import com.nosliw.common.constant.HAPEntityWithAttribute;

@HAPEntityWithAttribute
public interface HAPWithExternalValuePort extends HAPWithValuePort{

	@HAPAttribute
	public final static String EXTERNALVALUEPORT = "externalValuePort"; 
	
	HAPContainerValuePorts getExternalValuePorts();

}
