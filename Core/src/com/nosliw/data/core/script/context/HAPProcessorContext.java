package com.nosliw.data.core.script.context;

public class HAPProcessorContext {

	public static HAPContextGroup processContext(HAPContextGroup contextGroup, HAPContextGroup parentContextGroup, HAPConfigureContextProcessor configure, HAPEnvContextProcessor contextProcessorEnv) {
		
		//figure out all constant values in context
		contextGroup = HAPProcessorContextConstant.processConstantDefs(contextGroup, parentContextGroup, contextProcessorEnv);
		
		//solidate name in context  
		contextGroup = HAPProcessorContextSolidate.solidateContext(contextGroup, contextProcessorEnv);
		
		//process inheritance
		contextGroup = HAPProcessorContextInheritance.processContextInheritance(contextGroup, parentContextGroup, configure, contextProcessorEnv);
		
		//resolve relative context
		contextGroup = HAPProcessorContextRelative.processRelativeContextNode(contextGroup, parentContextGroup, configure, contextProcessorEnv);
		
		return contextGroup;
		
	}
	
}
