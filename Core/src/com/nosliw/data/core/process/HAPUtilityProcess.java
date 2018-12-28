package com.nosliw.data.core.process;

import com.nosliw.common.utils.HAPConstant;
import com.nosliw.data.core.script.context.HAPConfigureContextProcessor;
import com.nosliw.data.core.script.context.HAPContext;
import com.nosliw.data.core.script.context.HAPContextDefinitionElement;
import com.nosliw.data.core.script.context.HAPContextFlat;
import com.nosliw.data.core.script.context.HAPContextGroup;
import com.nosliw.data.core.script.context.HAPEnvContextProcessor;
import com.nosliw.data.core.script.context.HAPProcessorContext;
import com.nosliw.data.core.script.context.HAPUtilityContext;

public class HAPUtilityProcess {

	//process input configure for activity and generate flat context for activity
	public static HAPContextFlat processActivityInput(HAPContextGroup parentContext, HAPDefinitionDataAssociationGroup input, HAPEnvContextProcessor contextProcessorEnv) {
		String helpCategary = HAPConstant.UIRESOURCE_CONTEXTTYPE_PRIVATE;
		
		HAPConfigureContextProcessor configure = new HAPConfigureContextProcessor();
		configure.inheritMode = HAPUtilityContext.getContextGroupInheritMode(input.getInfo());

		//set input context to help categary
		HAPContextGroup context = new HAPContextGroup();
		
		//help context to store input context defintion
		HAPContext helpContext = new HAPContext();
		for(String eleName : input.getElementNames()) {
			helpContext.addElement(eleName, input.getElement(eleName));
		}
		
		//consolidate context in help context
		helpContext = consolidateContextRoot(helpContext);
		
		context.setContext(helpCategary, helpContext);

		//process input context
		context = HAPProcessorContext.process(context, parentContext, configure, contextProcessorEnv);
		
		return HAPUtilityContext.buildFlatContextFromContextGroup(context, null);
	}
	
	public static HAPContext processActivityOutput(HAPContext internalContext, HAPDefinitionDataAssociationGroup output, HAPEnvContextProcessor contextProcessorEnv) {
		String helpCategary = HAPConstant.UIRESOURCE_CONTEXTTYPE_PRIVATE;
		
		HAPContextGroup interalContextGroup = new HAPContextGroup();
		interalContextGroup.setContext(helpCategary, internalContext.cloneContext());
		
		HAPContextGroup outputContextGroup = new HAPContextGroup();
		outputContextGroup.setContext(helpCategary, output.cloneContext());
		
		HAPConfigureContextProcessor configure = new HAPConfigureContextProcessor();
		configure.inheritMode = HAPConfigureContextProcessor.VALUE_INHERITMODE_NONE;
		HAPContextGroup processedContextGroup = HAPProcessorContext.process(outputContextGroup, interalContextGroup, configure, contextProcessorEnv);
		HAPContext processedContext = processedContextGroup.getContext(helpCategary);
		
		HAPContext out = consolidateContextRoot(processedContext);
		return out;
	}
	
	//context root name can be like a.b.c and a.b.d
	//these two root name can be consolidated to one root name with a and child of b.c and b.d
	private static HAPContext consolidateContextRoot(HAPContext context) {
		HAPContext out = new HAPContext();
		
		for(String rootName : context.getElementNames()) {
			HAPContextDefinitionElement def = context.getElement(rootName).getDefinition();
			HAPUtilityContext.setDescendant(out, rootName, def);
		}
		return out;
	}
}
