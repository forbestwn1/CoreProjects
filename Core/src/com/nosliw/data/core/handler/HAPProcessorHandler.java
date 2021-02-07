package com.nosliw.data.core.handler;

import com.nosliw.common.utils.HAPConstant;

public class HAPProcessorHandler {

	public static HAPExecutableHandler processHandler(HAPHandler handler) {
		HAPExecutableHandler out = new HAPExecutableHandler(handler);
		for(HAPHandlerStep step : handler.getSteps()) {
			String stepType = step.getHandlerStepType();
			if(HAPConstant.HANDLERSTEP_TYPE_PROCESS.equals(stepType)) {
				
			}
		}
		
		return out;
	}
	
}
