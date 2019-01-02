package com.nosliw.data.core.process;

import java.util.LinkedHashMap;
import java.util.Map;

import com.nosliw.common.utils.HAPConstant;
import com.nosliw.data.core.script.context.HAPConfigureContextProcessor;
import com.nosliw.data.core.script.context.HAPContext;
import com.nosliw.data.core.script.context.HAPContextDefEleProcessor;
import com.nosliw.data.core.script.context.HAPContextDefinitionElement;
import com.nosliw.data.core.script.context.HAPContextDefinitionLeafRelative;
import com.nosliw.data.core.script.context.HAPContextFlat;
import com.nosliw.data.core.script.context.HAPContextGroup;
import com.nosliw.data.core.script.context.HAPContextPath;
import com.nosliw.data.core.script.context.HAPEnvContextProcessor;
import com.nosliw.data.core.script.context.HAPProcessorContext;
import com.nosliw.data.core.script.context.HAPUtilityContext;

public class HAPUtilityProcess {

	private static String helpCategary = HAPConstant.UIRESOURCE_CONTEXTTYPE_PRIVATE;

	//process input configure for activity and generate flat context for activity
	public static HAPDefinitionDataAssociationGroupExecutable processDataAssociation(HAPContextGroup parentContext, HAPDefinitionDataAssociationGroup input, HAPEnvContextProcessor contextProcessorEnv) {
		HAPDefinitionDataAssociationGroupExecutable out = new HAPDefinitionDataAssociationGroupExecutable(input);
		input = input.cloneDataAssocationGroup();
		
		HAPConfigureContextProcessor configure = new HAPConfigureContextProcessor();
		configure.inheritMode = HAPUtilityContext.getContextGroupInheritMode(input.getInfo()); 
//				HAPConfigureContextProcessor.VALUE_INHERITMODE_NONE; 

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
		out.setContext(HAPUtilityContext.buildFlatContextFromContextGroup(context, null));
		
		//build path mapping
		Map<String, String> pathMapping = new LinkedHashMap<String, String>();
		HAPContext helpContext1 = context.getContext(helpCategary);
		for(String eleName : helpContext1.getElementNames()) {
			pathMapping.putAll(HAPUtilityContext.buildRelativePathMapping(helpContext1.getElement(eleName), eleName));
		}
		out.setPathMapping(pathMapping);
		
		return out;
	}

	
	public static HAPDefinitionDataAssociationGroupExecutable processDataAssociation(HAPContext internalContext, HAPDefinitionDataAssociationGroup output, HAPEnvContextProcessor contextProcessorEnv) {
		
		HAPDefinitionDataAssociationGroupExecutable out = new HAPDefinitionDataAssociationGroupExecutable(output);
		output= output.cloneDataAssocationGroup();
		
		//build parent context group
		HAPContextGroup interalContextGroup = new HAPContextGroup();
		interalContextGroup.setContext(HAPConstant.UIRESOURCE_CONTEXTTYPE_PUBLIC, internalContext.cloneContext());
		
		//build context group to be processed
		HAPContextGroup outputContextGroup = new HAPContextGroup();
		outputContextGroup.setContext(helpCategary, output.cloneContext());
		
		HAPConfigureContextProcessor configure = new HAPConfigureContextProcessor();
		configure.inheritMode = HAPConfigureContextProcessor.VALUE_INHERITMODE_NONE;
		//process context to build context
		HAPContextGroup processedContextGroup = HAPProcessorContext.process(outputContextGroup, interalContextGroup, configure, contextProcessorEnv);

		//build context
		HAPContext processedContext = processedContextGroup.getContext(helpCategary);
		processedContext = consolidateContextRoot(processedContext);
		out.setContext(new HAPContextFlat(processedContext));
		
		//build path mapping
		Map<String, String> pathMapping = new LinkedHashMap<String, String>();
		//remove categary info in relative element
		for(String eleName : processedContext.getElementNames()) {
			HAPContextDefinitionElement contextDefEle = processedContext.getElement(eleName).getDefinition();
			HAPUtilityContext.processContextDefElement(contextDefEle, new HAPContextDefEleProcessor() {
				@Override
				public boolean process(HAPContextDefinitionElement ele, Object value) {
					if(ele.getType().equals(HAPConstant.CONTEXT_ELEMENTTYPE_RELATIVE)) {
						HAPContextDefinitionLeafRelative relativeEle = (HAPContextDefinitionLeafRelative)ele;
						HAPContextPath contextPath = relativeEle.getPath();
						relativeEle.setPath(new HAPContextPath(contextPath.getPath()));
					}
					return true;
				}

				@Override
				public boolean postProcess(HAPContextDefinitionElement ele, Object value) {		return true;	}
			}, null);
			
			pathMapping.putAll(HAPUtilityContext.buildRelativePathMapping(contextDefEle, eleName));
		}
		out.setPathMapping(pathMapping);

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
	
	public static String buildOutputVarialbeName(String name) {
		return "nosliw_" + name;
	}
}
