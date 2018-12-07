package com.nosliw.data.core.script.context;

public class HAPProcessorContext {

	//merge child context with parent context
	public static HAPContextGroup process1(HAPContextGroup contextGroup, HAPContextGroup parentContextGroup, HAPConfigureContextProcessor configure, HAPEnvContextProcessor contextProcessorEnv) {
		
		//figure out all constant values in context
		contextGroup = HAPProcessorContextConstant.process(contextGroup, parentContextGroup, configure.inheritMode, contextProcessorEnv);
		
		//solidate name in context  
		contextGroup = HAPProcessorContextSolidate.process(contextGroup, contextProcessorEnv);
		
		//process inheritance
		contextGroup = HAPProcessorContextInheritance.process(contextGroup, parentContextGroup, configure.inheritMode, contextProcessorEnv);
		
		return contextGroup;
		
	}
	
	public static HAPContextGroup process2(HAPContextGroup contextGroup, HAPContextGroup parentContextGroup, HAPConfigureContextProcessor configure, HAPEnvContextProcessor contextProcessorEnv) {
		
		//resolve relative context
		contextGroup = HAPProcessorContextRelative.process(contextGroup, parentContextGroup, configure, contextProcessorEnv);
		
		return contextGroup;
		
	}
}
