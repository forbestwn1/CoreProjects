package com.nosliw.data.core.task111;

public interface HAPManagerTaskSpecific {

	HAPProcessorTask getTaskProcessor();
	
	HAPExecutorTask getTaskExecutor();
	
	HAPDefinitionTask buildTaskDefinition(Object obj);
}
