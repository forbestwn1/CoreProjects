package com.nosliw.data.core.handler;

import com.nosliw.common.utils.HAPConstantShared;

public class HAPProcessorHandler {

	public static HAPExecutableHandler processHandler(HAPHandler handler) {
		HAPExecutableHandler out = new HAPExecutableHandler(handler);
		for(HAPHandlerStep step : handler.getSteps()) {
			String stepType = step.getHandlerStepType();
			if(HAPConstantShared.HANDLERSTEP_TYPE_PROCESS.equals(stepType)) {
				
			}
		}
		
		return out;
	}
	
}
