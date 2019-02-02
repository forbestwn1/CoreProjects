package com.nosliw.data.core.script.context;

import com.nosliw.common.utils.HAPConstant;

public class HAPProcessorContext {

	public static HAPContext process(HAPContext context, HAPContextGroup parentContextGroup, HAPConfigureContextProcessor configure, HAPRequirementContextProcessor contextProcessRequirement) {
		HAPContextGroup contextGroup = new HAPContextGroup();
		contextGroup.setContext(HAPConstant.UIRESOURCE_CONTEXTTYPE_PUBLIC, context);
		HAPContextGroup processed = process(contextGroup, parentContextGroup, configure, contextProcessRequirement);
		return processed.getContext(HAPConstant.UIRESOURCE_CONTEXTTYPE_PUBLIC);
	}
	
	public static HAPContextGroup process(HAPContextGroup contextGroup, HAPContextGroup parentContextGroup, HAPConfigureContextProcessor configure, HAPRequirementContextProcessor contextProcessRequirement) {
		HAPContextGroup out = processStatic(contextGroup, parentContextGroup, configure, contextProcessRequirement);
		out = processRelative(out, parentContextGroup, configure, contextProcessRequirement);
		out.processed();
		return out;
	}

	//merge child context with parent context
	public static HAPContextGroup processStatic(HAPContextGroup contextGroup, HAPContextGroup parentContextGroup, HAPConfigureContextProcessor configure, HAPRequirementContextProcessor contextProcessRequirement) {
		if(configure==null)  configure = new HAPConfigureContextProcessor();
		
		//figure out all constant values in context
		contextGroup = HAPProcessorContextConstant.process(contextGroup, parentContextGroup, configure.inheritMode, contextProcessRequirement);
		
		//solidate name in context  
		contextGroup = HAPProcessorContextSolidate.process(contextGroup, contextProcessRequirement);
		
		//process inheritance
		contextGroup = HAPProcessorContextInheritance.process(contextGroup, parentContextGroup, configure.inheritMode, contextProcessRequirement);
		
		return contextGroup;
	}
	
	public static HAPContextGroup processRelative(HAPContextGroup contextGroup, HAPContextGroup parentContextGroup, HAPConfigureContextProcessor configure, HAPRequirementContextProcessor contextProcessRequirement) {
		
		if(configure==null)  configure = new HAPConfigureContextProcessor();
		
		//resolve relative context
		contextGroup = HAPProcessorContextRelative.process(contextGroup, parentContextGroup, configure, contextProcessRequirement);
		
		return contextGroup;
		
	}
}
