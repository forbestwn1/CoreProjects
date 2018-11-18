package com.nosliw.data.core.process;

public interface HAPPluginActivity {

	HAPProcessorActivity getStepProcessor();
	
	HAPDefinitionActivity buildStepDefinition(Object obj);
	
}
