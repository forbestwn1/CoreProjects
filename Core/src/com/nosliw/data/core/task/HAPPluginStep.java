package com.nosliw.data.core.task;

public interface HAPPluginStep {

	HAPProcessorStep getStepProcessor();
	
	HAPDefinitionStep buildStepDefinition(Object obj);
	
}
