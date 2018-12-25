package com.nosliw.data.core.process;

import com.nosliw.common.utils.HAPConstant;
import com.nosliw.data.core.script.context.HAPConfigureContextProcessor;
import com.nosliw.data.core.script.context.HAPContextFlat;
import com.nosliw.data.core.script.context.HAPContextGroup;
import com.nosliw.data.core.script.context.HAPEnvContextProcessor;
import com.nosliw.data.core.script.context.HAPProcessorContext;
import com.nosliw.data.core.script.context.HAPUtilityContext;

public class HAPUtilityProcess {

	//process input configure for activity and generate flat context for activity
	public static HAPContextFlat processActivityInput(HAPContextGroup parentContext, HAPDefinitionDataAssociationGroup input, HAPEnvContextProcessor contextProcessorEnv) {
		HAPConfigureContextProcessor configure = new HAPConfigureContextProcessor();
		configure.inheritMode = HAPUtilityContext.getContextGroupInheritMode(input.getInfo());

		HAPContextGroup context = new HAPContextGroup();
		for(String eleName : input.getElementNames()) {
			context.addElement(eleName, input.getElement(eleName), HAPConstant.UIRESOURCE_CONTEXTTYPE_PRIVATE);
		}
		
		context = HAPProcessorContext.process1(context, parentContext, configure, contextProcessorEnv);
		context = HAPProcessorContext.processRelative(context, parentContext, configure, contextProcessorEnv);
		
		return HAPUtilityContext.buildFlatContextFromContextGroup(context, null);
	}
	
	
}
