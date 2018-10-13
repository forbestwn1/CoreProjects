package com.nosliw.data.core.task111.expression;

public interface HAPManagerStep {

	HAPProcessorStep getStepProcessor();
	
	HAPExecutorStep getStepExecutor();
	
	HAPDefinitionStep buildStepDefinition(Object obj);
	
}
