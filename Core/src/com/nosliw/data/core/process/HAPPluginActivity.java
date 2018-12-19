package com.nosliw.data.core.process;

public interface HAPPluginActivity {

	String getType();
	
	HAPProcessorActivity getActivityProcessor();
	
	HAPDefinitionActivity buildActivityDefinition(Object obj);
	
	String getScript();
}
