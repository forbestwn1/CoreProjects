package com.nosliw.data.core.service.use;

import com.nosliw.data.core.script.context.HAPConfigureContextProcessor;
import com.nosliw.data.core.script.context.HAPContext;
import com.nosliw.data.core.script.context.HAPContextGroup;
import com.nosliw.data.core.script.context.HAPProcessorContext;
import com.nosliw.data.core.script.context.HAPRequirementContextProcessor;
import com.nosliw.data.core.script.context.HAPUtilityContext;

public class HAPProcessorServiceUse {

	public HAPExecutableServiceUse process(
			HAPDefinitionServiceUse definition, 
			HAPContextGroup globalContext, 
			HAPConfigureContextProcessor configure, 
			HAPRequirementContextProcessor contextProcessRequirement) {
		HAPExecutableServiceUse out = new HAPExecutableServiceUse(definition);
		
		HAPContext processedParms = HAPProcessorContext.process(definition.getParms(), globalContext, configure, contextProcessRequirement);
		
		
		
		return out;
	}
	
}
