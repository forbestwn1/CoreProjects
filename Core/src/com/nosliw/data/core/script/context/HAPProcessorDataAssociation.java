package com.nosliw.data.core.script.context;

import java.util.LinkedHashMap;
import java.util.Map;

import com.nosliw.common.updatename.HAPUpdateNameMap;
import com.nosliw.common.utils.HAPConstant;
import com.nosliw.data.core.expression.HAPMatcherUtility;
import com.nosliw.data.core.expression.HAPMatchers;

public class HAPProcessorDataAssociation {

	private static String INFO_MAPPEDROOT = "mappedRoot";

	private static String helpCategary = HAPConstant.UIRESOURCE_CONTEXTTYPE_PRIVATE;

	public static HAPExecutableDataAssociationGroup processDataAssociation(HAPContextGroup inputContextGroup, HAPDefinitionDataAssociationGroup dataAssociation, HAPRequirementContextProcessor contextProcessRequirement) {
		HAPExecutableDataAssociationGroup out = new HAPExecutableDataAssociationGroup(dataAssociation);
		processDataAssociation(inputContextGroup, dataAssociation, out, contextProcessRequirement);
		return out;
	}
	
	//process input configure for activity and generate flat context for activity
	public static void processDataAssociation(HAPContextGroup inputContextGroup, HAPDefinitionDataAssociationGroup dataAssociation, HAPExecutableDataAssociationGroup out, HAPRequirementContextProcessor contextProcessRequirement) {
		processDataAssocationContext(out, inputContextGroup, dataAssociation, contextProcessRequirement);
		
		//build path mapping according to mapped context
		Map<String, String> pathMapping = new LinkedHashMap<String, String>();
		for(String eleName : out.getContext().getContext().getElementNames()) {
			HAPContextDefinitionRoot root = out.getContext().getContext().getElement(eleName);
			if(isMappedRoot(root)) {
				pathMapping.putAll(HAPUtilityContext.buildRelativePathMapping(root, buildEleNameAccordingToFlat(eleName, dataAssociation.isFlatOutput())));
			}
		}
		out.setPathMapping(pathMapping);
	}

	public static HAPExecutableDataAssociationGroup processDataAssociation(HAPContext inputContext, HAPDefinitionDataAssociationGroup dataAssociation, HAPRequirementContextProcessor contextProcessRequirement) {
		HAPExecutableDataAssociationGroup out = new HAPExecutableDataAssociationGroup(dataAssociation);
		processDataAssociation(inputContext, dataAssociation, out, contextProcessRequirement);
		return out;
	}
	
	public static void processDataAssociation(HAPContext inputContext, HAPDefinitionDataAssociationGroup dataAssociation, HAPExecutableDataAssociationGroup out, HAPRequirementContextProcessor contextProcessRequirement) {
		//build input context group
		HAPContextGroup inputContextGroup = new HAPContextGroup();
		inputContextGroup.setContext(HAPConstant.UIRESOURCE_CONTEXTTYPE_PUBLIC, inputContext.cloneContext());

		processDataAssocationContext(out, inputContextGroup, dataAssociation, contextProcessRequirement);

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
				
				pathMapping.putAll(HAPUtilityContext.buildRelativePathMapping(root, buildEleNameAccordingToFlat(eleName, dataAssociation.isFlatOutput())));
			}
		}
		out.setPathMapping(pathMapping);
	}

	public static HAPHAPExecutableDataAssociationGroupWithTarget processDataAssociation(HAPContext inputContext, HAPDefinitionDataAssociationGroup dataAssociation, HAPContextGroup targetContext, HAPRequirementContextProcessor contextProcessRequirement) {
		HAPHAPExecutableDataAssociationGroupWithTarget out = new HAPHAPExecutableDataAssociationGroupWithTarget(dataAssociation);
		processDataAssociation(inputContext, out, targetContext, contextProcessRequirement);		
		return out;
	}
	
	public static void processDataAssociation(HAPContext inputContext, HAPHAPExecutableDataAssociationGroupWithTarget backToGlobal, HAPContextGroup targetContext, HAPRequirementContextProcessor contextProcessRequirement) {
		HAPDefinitionDataAssociationGroup dataAssociation = backToGlobal.getDefinition();
		
		//process data association
		HAPProcessorDataAssociation.processDataAssociation(inputContext, dataAssociation, backToGlobal, contextProcessRequirement);

		//process result
		Map<String, String> nameMapping = new LinkedHashMap<String, String>();
		HAPContext outputContext = backToGlobal.getContext().getContext();
		for(String rootName : outputContext.getElementNames()) {
			//find matching variable in parent context
			HAPInfoRelativeContextResolve resolvedInfo = HAPUtilityContext.resolveReferencedParentContextNode(new HAPContextPath(rootName), targetContext, null, null);
			HAPContextDefinitionRootId contextVarRootId = resolvedInfo.path.getRootElementId();
			//merge back to context variable
			Map<String, HAPMatchers> matchers = HAPUtilityContext.mergeContextRoot(targetContext.getElement(contextVarRootId), outputContext.getElement(rootName), true, contextProcessRequirement);
			//matchers when merge back to context variable
			for(String matchPath :matchers.keySet()) {
				backToGlobal.addOutputMatchers(new HAPContextPath(contextVarRootId, matchPath).getFullPath(), HAPMatcherUtility.reversMatchers(matchers.get(matchPath)));
			}
			
			//root variable name --- root variable full name
			nameMapping.put(rootName, contextVarRootId.getFullName());
		}
		
		//update variable names with full name 
		backToGlobal.updateOutputRootName(new HAPUpdateNameMap(nameMapping));
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
	
	private static String buildEleNameAccordingToFlat(String eleName, boolean isFlat) {
		HAPContextDefinitionRootId eleId = new HAPContextDefinitionRootId(eleName);
		if(isFlat)  return eleId.getFullName();
		else return eleId.getPath();
	}
	

}
