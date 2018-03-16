package com.nosliw.data.core.task.expression;

public interface HAPManagerStep {

	HAPProcessorStep getStepProcessor();
	
	HAPExecutorStep getStepExecutor();
	
	HAPDefinitionStep buildStepDefinition(Object obj);
	
}
