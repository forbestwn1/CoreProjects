package com.nosliw.data.core.process;

public interface HAPPluginActivity {

	HAPProcessorActivity getActivityProcessor();
	
	HAPDefinitionActivity buildActivityDefinition(Object obj);
	
	String getScript();
}
