package com.nosliw.data.core.process;

import java.util.LinkedHashMap;
import java.util.Map;

import com.nosliw.common.utils.HAPConstant;
import com.nosliw.data.core.script.context.HAPConfigureContextProcessor;
import com.nosliw.data.core.script.context.HAPContext;
import com.nosliw.data.core.script.context.HAPContextDefEleProcessor;
import com.nosliw.data.core.script.context.HAPContextDefinitionElement;
import com.nosliw.data.core.script.context.HAPContextDefinitionLeafRelative;
import com.nosliw.data.core.script.context.HAPContextDefinitionRoot;
import com.nosliw.data.core.script.context.HAPContextFlat;
import com.nosliw.data.core.script.context.HAPContextGroup;
import com.nosliw.data.core.script.context.HAPContextPath;
import com.nosliw.data.core.script.context.HAPProcessorContext;
import com.nosliw.data.core.script.context.HAPRequirementContextProcessor;
import com.nosliw.data.core.script.context.HAPUtilityContext;

public class HAPProcessorDataAssociation {

	public static String INFO_MAPPEDROOT = "mappedRoot";

	private static String helpCategary = HAPConstant.UIRESOURCE_CONTEXTTYPE_PRIVATE;

	//process input configure for activity and generate flat context for activity
	public static HAPExecutableDataAssociationGroup processDataAssociation(HAPContextGroup inputContext, HAPDefinitionDataAssociationGroup dataAssociation, HAPRequirementContextProcessor contextProcessRequirement) {
		HAPExecutableDataAssociationGroup out = new HAPExecutableDataAssociationGroup(dataAssociation);
		
		HAPConfigureContextProcessor configure = new HAPConfigureContextProcessor();
		configure.inheritMode = HAPUtilityContext.getContextGroupInheritMode(dataAssociation.getInfo()); 

		//set input context to help categary
		HAPContextGroup dataAssociationContextGroup = new HAPContextGroup();
		//consolidate data association context and put it in help context
		dataAssociationContextGroup.setContext(helpCategary, HAPUtilityContext.consolidateContextRoot(dataAssociation));
		
		//process data association context group with input context
		dataAssociationContextGroup = HAPProcessorContext.process(dataAssociationContextGroup, inputContext, configure, contextProcessRequirement);
		
		//build flat context without mapped context
		HAPContext mappedContext = dataAssociationContextGroup.removeContext(helpCategary);
		out.setContext(HAPUtilityContext.buildFlatContextFromContextGroup(dataAssociationContextGroup, null));
		
		//then add mapped context to flat context
		for(String eleName : mappedContext.getElementNames()) {
			HAPContextDefinitionRoot rootEle = mappedContext.getElement(eleName);
			markAsMappedRoot(rootEle);
			out.getContext().addElement(eleName, rootEle);
		}
		
		//build path mapping according to mapped context
		Map<String, String> pathMapping = new LinkedHashMap<String, String>();
		for(String eleName : mappedContext.getElementNames()) {
			pathMapping.putAll(HAPUtilityContext.buildRelativePathMapping(mappedContext.getElement(eleName), eleName));
		}
		out.setPathMapping(pathMapping);
		
		return out;
	}

	public static HAPExecutableDataAssociationGroup processDataAssociation(HAPContext inputContext, HAPDefinitionDataAssociationGroup mapping, HAPRequirementContextProcessor contextProcessRequirement) {
		HAPUtilityContext.setContextGroupInheritModeNone(mapping.getInfo());
		
		HAPExecutableDataAssociationGroup out = new HAPExecutableDataAssociationGroup(mapping);
		mapping= mapping.cloneDataAssocationGroup();
		
		//build parent context group
		HAPContextGroup inputContextGroup = new HAPContextGroup();
		inputContextGroup.setContext(HAPConstant.UIRESOURCE_CONTEXTTYPE_PUBLIC, inputContext.cloneContext());
		
		//build context group to be processed
		HAPContextGroup mappingContextGroup = new HAPContextGroup();
		mappingContextGroup.setContext(helpCategary, mapping.cloneContext());
		
		HAPConfigureContextProcessor configure = new HAPConfigureContextProcessor();
		configure.inheritMode = HAPUtilityContext.getContextGroupInheritMode(mapping.getInfo()); 
		//process context to build context
		HAPContextGroup outputContextGroup = HAPProcessorContext.process(mappingContextGroup, inputContextGroup, configure, contextProcessRequirement);

		//build context
		HAPContext outputContext = outputContextGroup.getContext(helpCategary);
		outputContext = consolidateContextRoot(outputContext);
		markAsMappedRoot(outputContext);
		out.setContext(new HAPContextFlat(outputContext));
		
		//build path mapping
		Map<String, String> pathMapping = new LinkedHashMap<String, String>();
		for(String eleName : outputContext.getElementNames()) {
			HAPContextDefinitionElement contextDefEle = outputContext.getElement(eleName).getDefinition();
			//remove categary info in relative element first
			HAPUtilityContext.processContextDefElement(contextDefEle, new HAPContextDefEleProcessor() {
				@Override
				public boolean process(HAPContextDefinitionElement ele, Object value) {
					if(ele.getType().equals(HAPConstant.CONTEXT_ELEMENTTYPE_RELATIVE)) {
						HAPContextDefinitionLeafRelative relativeEle = (HAPContextDefinitionLeafRelative)ele;
						relativeEle.setPath(new HAPContextPath(relativeEle.getPath().getPath()));
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
	
	public static void markAsMappedRoot(HAPContextDefinitionRoot root) {
		root.getInfo().setValue(INFO_MAPPEDROOT, "yes");
	}

	public static boolean isMappedRoot(HAPContextDefinitionRoot root) {
		return root.getInfo().getValue(INFO_MAPPEDROOT)!=null;
	}
}
