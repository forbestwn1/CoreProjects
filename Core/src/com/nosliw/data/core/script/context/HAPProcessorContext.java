package com.nosliw.data.core.script.context;

public class HAPProcessorContext {

	public static HAPContextGroup processContext(HAPContextGroup contextGroup, HAPContextGroup parentContextGroup, HAPConfigureContextProcessor configure, HAPEnvContextProcessor contextProcessorEnv) {
		
		contextGroup = HAPProcessorContextConstant.processConstantDefs(contextGroup, parentContextGroup, contextProcessorEnv);
		
		contextGroup = HAPProcessorContextSolidate.solidateContext(contextGroup, contextProcessorEnv);
		
		contextGroup = HAPProcessorContextInheritance.processContextInheritance(contextGroup, parentContextGroup, configure, contextProcessorEnv);
		
		contextGroup = HAPProcessorContextRelative.processRelativeContextNode(contextGroup, parentContextGroup, configure, contextProcessorEnv);
		
		return contextGroup;
		
	}
	
}
