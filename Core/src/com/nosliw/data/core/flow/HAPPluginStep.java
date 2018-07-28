package com.nosliw.data.core.flow;

public interface HAPPluginStep {

	HAPProcessorStep getStepProcessor();
	
	HAPDefinitionStep buildStepDefinition(Object obj);
	
}
