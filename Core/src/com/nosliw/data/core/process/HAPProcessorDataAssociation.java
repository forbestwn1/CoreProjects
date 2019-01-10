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
import com.nosliw.data.core.script.context.HAPContextGroup;
import com.nosliw.data.core.script.context.HAPContextPath;
import com.nosliw.data.core.script.context.HAPProcessorContext;
import com.nosliw.data.core.script.context.HAPRequirementContextProcessor;
import com.nosliw.data.core.script.context.HAPUtilityContext;

public class HAPProcessorDataAssociation {

	public static String INFO_MAPPEDROOT = "mappedRoot";

	private static String helpCategary = HAPConstant.UIRESOURCE_CONTEXTTYPE_PRIVATE;

	//process input configure for activity and generate flat context for activity
	public static HAPExecutableDataAssociationGroup processDataAssociation(HAPContextGroup inputContextGroup, HAPDefinitionDataAssociationGroup dataAssociation, HAPRequirementContextProcessor contextProcessRequirement) {
		HAPExecutableDataAssociationGroup out = new HAPExecutableDataAssociationGroup(dataAssociation);
		
		processDataAssocationContext(out, inputContextGroup, dataAssociation, contextProcessRequirement);
		
/*		
		//set input context to help categary
		HAPContextGroup dataAssociationContextGroup = new HAPContextGroup();
		//consolidate data association context and put it in help context
		dataAssociationContextGroup.setContext(helpCategary, HAPUtilityContext.consolidateContextRoot(dataAssociation));
		
		//process data association context group with input context
		HAPConfigureContextProcessor configure = new HAPConfigureContextProcessor();
		configure.inheritMode = HAPUtilityContext.getContextGroupInheritMode(dataAssociation.getInfo()); 
		dataAssociationContextGroup = HAPProcessorContext.process(dataAssociationContextGroup, inputContextGroup, configure, contextProcessRequirement);
		
		//build flat context without mapped context
		HAPContext mappedContext = dataAssociationContextGroup.removeContext(helpCategary);
		out.setContext(HAPUtilityContext.buildFlatContextFromContextGroup(dataAssociationContextGroup, null));
		
		//then add mapped context to flat context
		for(String eleName : mappedContext.getElementNames()) {
			HAPContextDefinitionRoot rootEle = mappedContext.getElement(eleName);
			markAsMappedRoot(rootEle);
			out.getContext().addElement(eleName, rootEle);
		}
*/
		
		//build path mapping according to mapped context
		Map<String, String> pathMapping = new LinkedHashMap<String, String>();
		for(String eleName : out.getContext().getContext().getElementNames()) {
			HAPContextDefinitionRoot root = out.getContext().getContext().getElement(eleName);
			if(isMappedRoot(root)) {
				pathMapping.putAll(HAPUtilityContext.buildRelativePathMapping(root, eleName));
			}
		}
		out.setPathMapping(pathMapping);
		
		return out;
	}

	public static HAPExecutableDataAssociationGroup processDataAssociation(HAPContext inputContext, HAPDefinitionDataAssociationGroup dataAssociation, HAPRequirementContextProcessor contextProcessRequirement) {
		HAPExecutableDataAssociationGroup out = new HAPExecutableDataAssociationGroup(dataAssociation);
		
		//build input context group
		HAPContextGroup inputContextGroup = new HAPContextGroup();
		inputContextGroup.setContext(HAPConstant.UIRESOURCE_CONTEXTTYPE_PUBLIC, inputContext.cloneContext());

		processDataAssocationContext(out, inputContextGroup, dataAssociation, contextProcessRequirement);

/*		
		//build context group to be processed
		HAPContextGroup dataAssociationContextGroup = new HAPContextGroup();
		dataAssociationContextGroup.setContext(helpCategary, HAPUtilityContext.consolidateContextRoot(dataAssociation));
		
		//process context to build context
		HAPConfigureContextProcessor configure = new HAPConfigureContextProcessor();
		configure.inheritMode = HAPUtilityContext.getContextGroupInheritMode(dataAssociation.getInfo()); 
		dataAssociationContextGroup = HAPProcessorContext.process(dataAssociationContextGroup, inputContextGroup, configure, contextProcessRequirement);

		//build flat context without mapped context
		HAPContext mappedContext = dataAssociationContextGroup.removeContext(helpCategary);
		out.setContext(HAPUtilityContext.buildFlatContextFromContextGroup(dataAssociationContextGroup, null));
		
		//then add mapped context to flat context
		for(String eleName : mappedContext.getElementNames()) {
			HAPContextDefinitionRoot rootEle = mappedContext.getElement(eleName);
			markAsMappedRoot(rootEle);
			out.getContext().addElement(eleName, rootEle);
		}
*/		
		
		//build path mapping
		Map<String, String> pathMapping = new LinkedHashMap<String, String>();
		for(String eleName : out.getContext().getContext().getElementNames()) {
			HAPContextDefinitionRoot root = out.getContext().getContext().getElement(eleName);
			//only root that do mapping
			if(isMappedRoot(root)) {
				//remove categary info in relative element first
				HAPUtilityContext.processContextDefElement(root.getDefinition(), new HAPContextDefEleProcessor() {
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
				
				pathMapping.putAll(HAPUtilityContext.buildRelativePathMapping(root, eleName));
			}
		}
		out.setPathMapping(pathMapping);

		return out;
	}
	
	private static void processDataAssocationContext(HAPExecutableDataAssociationGroup out, HAPContextGroup inputContextGroup, HAPDefinitionDataAssociationGroup dataAssociation, HAPRequirementContextProcessor contextProcessRequirement) {
		//build context group to be processed
		HAPContextGroup dataAssociationContextGroup = new HAPContextGroup();
		dataAssociationContextGroup.setContext(helpCategary, HAPUtilityContext.consolidateContextRoot(dataAssociation));
		
		//process context to build context
		HAPConfigureContextProcessor configure = new HAPConfigureContextProcessor();
		configure.inheritMode = HAPUtilityContext.getContextGroupInheritMode(dataAssociation.getInfo()); 
		dataAssociationContextGroup = HAPProcessorContext.process(dataAssociationContextGroup, inputContextGroup, configure, contextProcessRequirement);

		//build flat context without mapped context
		HAPContext mappedContext = dataAssociationContextGroup.removeContext(helpCategary);
		out.setContext(HAPUtilityContext.buildFlatContextFromContextGroup(dataAssociationContextGroup, null));
		
		//then add mapped context to flat context
		for(String eleName : mappedContext.getElementNames()) {
			HAPContextDefinitionRoot rootEle = mappedContext.getElement(eleName);
			markAsMappedRoot(rootEle);
			out.getContext().addElement(eleName, rootEle);
		}
	}
	
	public static void markAsMappedRoot(HAPContextDefinitionRoot root) {
		root.getInfo().setValue(INFO_MAPPEDROOT, "yes");
	}

	public static boolean isMappedRoot(HAPContextDefinitionRoot root) {
		return root.getInfo().getValue(INFO_MAPPEDROOT)!=null;
	}
	
	public static void markAsMappedContext(HAPContext context) {
		for(String rootName : context.getElementNames()) {
			markAsMappedRoot(context.getElement(rootName));
		}
	}
}
