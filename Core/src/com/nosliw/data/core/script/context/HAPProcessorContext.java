package com.nosliw.data.core.script.context;

public class HAPProcessorContext {

	//merge child context with parent context
	public static HAPContextGroup process(HAPContextGroup contextGroup, HAPContextGroup parentContextGroup, HAPConfigureContextProcessor configure, HAPEnvContextProcessor contextProcessorEnv) {
		
		//figure out all constant values in context
		contextGroup = HAPProcessorContextConstant.process(contextGroup, parentContextGroup, contextProcessorEnv);
		
		//solidate name in context  
		contextGroup = HAPProcessorContextSolidate.process(contextGroup, contextProcessorEnv);
		
		//process inheritance
		contextGroup = HAPProcessorContextInheritance.process(contextGroup, parentContextGroup, configure, contextProcessorEnv);
		
		//resolve relative context
		contextGroup = HAPProcessorContextRelative.process(contextGroup, parentContextGroup, configure, contextProcessorEnv);
		
		return contextGroup;
		
	}
	
}
