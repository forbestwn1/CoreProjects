package com.nosliw.data.core.script.context;

import com.nosliw.common.utils.HAPConstant;

public class HAPProcessorContext {

	public static HAPContext process(HAPContext context, HAPParentContext parent, HAPConfigureContextProcessor configure, HAPRequirementContextProcessor contextProcessRequirement) {
		if(configure==null)  configure = new HAPConfigureContextProcessor();
		HAPContextGroup contextGroup = new HAPContextGroup();
		contextGroup.setContext(HAPConstant.UIRESOURCE_CONTEXTTYPE_PUBLIC, context);
		HAPContextGroup processed = process(contextGroup, parent, configure, contextProcessRequirement);
		return processed.getContext(HAPConstant.UIRESOURCE_CONTEXTTYPE_PUBLIC);
	}
	
	public static HAPContextGroup process(HAPContextGroup contextGroup, HAPParentContext parent, HAPConfigureContextProcessor configure, HAPRequirementContextProcessor contextProcessRequirement) {
		if(configure==null)  configure = new HAPConfigureContextProcessor();
		HAPContextGroup out = processStatic(contextGroup, parent, configure, contextProcessRequirement);
		out = processRelative(out, parent, configure, contextProcessRequirement);
		out.processed();
		return out;
	}

	//merge child context with parent context
	public static HAPContextGroup processStatic(HAPContextGroup contextGroup, HAPParentContext parent, HAPConfigureContextProcessor configure, HAPRequirementContextProcessor contextProcessRequirement) {
		if(configure==null)  configure = new HAPConfigureContextProcessor();
		
		//figure out all constant values in context
		contextGroup = HAPProcessorContextConstant.process(contextGroup, parent, configure.inheritMode, contextProcessRequirement);
		
		//solidate name in context  
		contextGroup = HAPProcessorContextSolidate.process(contextGroup, contextProcessRequirement);
		
		//process inheritance
		contextGroup = HAPProcessorContextVariableInheritance.process(contextGroup, parent, configure.inheritMode, contextProcessRequirement);
		
		return contextGroup;
	}
	
	public static HAPContextGroup processRelative(HAPContextGroup contextGroup, HAPParentContext parent, HAPConfigureContextProcessor configure, HAPRequirementContextProcessor contextProcessRequirement) {
		if(configure==null)  configure = new HAPConfigureContextProcessor();
		
		//resolve relative context
		contextGroup = HAPProcessorContextRelative.process(contextGroup, parent, configure, contextProcessRequirement);
		
		return contextGroup;
		
	}
}
