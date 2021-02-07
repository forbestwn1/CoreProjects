package com.nosliw.data.core.handler;

import com.nosliw.common.constant.HAPAttribute;
import com.nosliw.common.constant.HAPEntityWithAttribute;
import com.nosliw.common.serialization.HAPSerializable;

@HAPEntityWithAttribute
public interface HAPHandlerStep extends HAPSerializable{

	@HAPAttribute
	public static String STEPTYPE = "stepType";

	String getHandlerStepType();
	
	HAPHandlerStep cloneHandlerStep();
}
