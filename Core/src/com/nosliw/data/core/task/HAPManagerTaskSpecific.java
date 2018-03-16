package com.nosliw.data.core.task;

public interface HAPManagerTaskSpecific {

	HAPProcessorTask getTaskProcessor();
	
	HAPExecutorTask getTaskExecutor();
	
	HAPDefinitionTask buildTaskDefinition(Object obj);
}
