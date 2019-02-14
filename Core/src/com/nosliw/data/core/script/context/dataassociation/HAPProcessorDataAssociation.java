package com.nosliw.data.core.script.context.dataassociation;

import java.util.LinkedHashMap;
import java.util.Map;

import com.nosliw.common.utils.HAPConstant;
import com.nosliw.data.core.matcher.HAPMatcherUtility;
import com.nosliw.data.core.matcher.HAPMatchers;
import com.nosliw.data.core.script.context.HAPContext;
import com.nosliw.data.core.script.context.HAPContextDefEleProcessor;
import com.nosliw.data.core.script.context.HAPContextDefinitionElement;
import com.nosliw.data.core.script.context.HAPContextDefinitionLeafRelative;
import com.nosliw.data.core.script.context.HAPContextDefinitionRoot;
import com.nosliw.data.core.script.context.HAPContextDefinitionRootId;
import com.nosliw.data.core.script.context.HAPContextFlat;
import com.nosliw.data.core.script.context.HAPContextGroup;
import com.nosliw.data.core.script.context.HAPContextPath;
import com.nosliw.data.core.script.context.HAPInfoRelativeContextResolve;
import com.nosliw.data.core.script.context.HAPProcessorContext;
import com.nosliw.data.core.script.context.HAPRequirementContextProcessor;
import com.nosliw.data.core.script.context.HAPUtilityContext;
import com.nosliw.data.core.script.context.HAPUtilityContextInfo;

public class HAPProcessorDataAssociation {

	private static String INFO_MAPPEDROOT = "mappedRoot";

	private static String helpCategary = HAPConstant.UIRESOURCE_CONTEXTTYPE_PRIVATE;

	public static HAPExecutableDataAssociationGroup processDataAssociation(HAPDataAssociationIO input, HAPDefinitionDataAssociationGroup dataAssociation, HAPRequirementContextProcessor contextProcessRequirement) {
		HAPExecutableDataAssociationGroup out = new HAPExecutableDataAssociationGroup(dataAssociation);
		processDataAssociation(input, out, contextProcessRequirement);
		return out;
	}

	public static void processDataAssociation(HAPDataAssociationIO input, HAPExecutableDataAssociationGroup out, HAPRequirementContextProcessor contextProcessRequirement) {
		if(input instanceof HAPContext)  processDataAssociation((HAPContext)input, out, contextProcessRequirement);
		if(input instanceof HAPContextGroup)  processDataAssociation((HAPContextGroup)input, out, contextProcessRequirement);
	}
	
	public static HAPExecutableDataAssociationGroup processDataAssociation(HAPContextGroup inputContextGroup, HAPDefinitionDataAssociationGroup dataAssociation, HAPRequirementContextProcessor contextProcessRequirement) {
		HAPExecutableDataAssociationGroup out = new HAPExecutableDataAssociationGroup(dataAssociation);
		processDataAssociation(inputContextGroup, out, contextProcessRequirement);
		return out;
	}

	public static HAPExecutableDataAssociationGroup processDataAssociation(HAPContext inputContext, HAPDefinitionDataAssociationGroup dataAssociation, HAPRequirementContextProcessor contextProcessRequirement) {
		HAPExecutableDataAssociationGroup out = new HAPExecutableDataAssociationGroup(dataAssociation);
		processDataAssociation(inputContext, out, contextProcessRequirement);
		return out;
	}
	
	//process input configure for activity and generate flat context for activity
	public static void processDataAssociation(HAPContextGroup inputContextGroup, HAPExecutableDataAssociationGroup out, HAPRequirementContextProcessor contextProcessRequirement) {
		HAPDefinitionDataAssociationGroup dataAssociation = out.getDefinition();
		boolean isFlatOutput = out.isFlatOutput();
		out.setIsFlatInput(false);
		processDataAssocationContext(out, inputContextGroup, dataAssociation, contextProcessRequirement);
		buildPathMappingInDataAssociation(out, out.isFlatInput(), isFlatOutput);
	}

	public static void processDataAssociation(HAPContext inputContext, HAPExecutableDataAssociationGroup out, HAPRequirementContextProcessor contextProcessRequirement) {
		HAPDefinitionDataAssociationGroup dataAssociation = out.getDefinition();
		boolean isFlatOutput = out.isFlatOutput();
		out.setIsFlatInput(true);
		//build input context group
		HAPContextGroup inputContextGroup = new HAPContextGroup();
		inputContextGroup.setContext(HAPConstant.UIRESOURCE_CONTEXTTYPE_PUBLIC, inputContext.cloneContext());

		processDataAssocationContext(out, inputContextGroup, dataAssociation, contextProcessRequirement);

		HAPContext flatContext = out.getContext().getContext();
		//remove categary info in relative element first as categary information is fake one
		for(String eleName : flatContext.getElementNames()) {
			HAPUtilityContext.processContextDefElement(flatContext.getElement(eleName).getDefinition(), new HAPContextDefEleProcessor() {
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
		}

		buildPathMappingInDataAssociation(out, out.isFlatInput(), isFlatOutput);
	}

	public static HAPExecutableDataAssociationGroupWithTarget processDataAssociation(HAPDataAssociationIO input, HAPDefinitionDataAssociationGroup dataAssociation, HAPDataAssociationIO output, boolean modifyStructure, HAPRequirementContextProcessor contextProcessRequirement) {
		HAPExecutableDataAssociationGroupWithTarget out = new HAPExecutableDataAssociationGroupWithTarget(dataAssociation);
		processDataAssociation(input, out, output, modifyStructure, contextProcessRequirement);
		return out;
	}
	
	public static void processDataAssociation(HAPDataAssociationIO input, HAPExecutableDataAssociationGroupWithTarget out, HAPDataAssociationIO output, boolean modifyStructure, HAPRequirementContextProcessor contextProcessRequirement) {
		if(output instanceof HAPContext)   out.setIsFlatOutput(true);
		else if(output instanceof HAPContextGroup)   out.setIsFlatOutput(false);
		
		if(output instanceof HAPContextGroup) {
			//update root name with full name (containing categary and element name)
			HAPDefinitionDataAssociationGroup origin = out.getDefinition();
			HAPDefinitionDataAssociationGroup updated = origin.cloneDataAssocationGroupBase();
			for(String eleName : origin.getElementNames()) {
				String updatedName = eleName;
				HAPInfoRelativeContextResolve resolvedInfo = HAPUtilityContext.resolveReferencedParentContextNode(new HAPContextPath(eleName), (HAPContextGroup)output, null, null);
				if(resolvedInfo!=null) 	updatedName = resolvedInfo.path.getRootElementId().getFullName();
				updated.addElement(updatedName, origin.getElement(eleName));
			}
			out.setDefinition(updated);
		}
		
		//process data association
		processDataAssociation(input, out, contextProcessRequirement);

		//process result
		if(output instanceof HAPContext) {
			HAPContext targetContext = (HAPContext)output;
			HAPContext outputContext = out.getContext().getContext();
			for(String rootName : outputContext.getElementNames()) {
				//merge back to context variable
				if(targetContext.getElement(rootName)!=null) {
					Map<String, HAPMatchers> matchers = HAPUtilityContext.mergeContextRoot(targetContext.getElement(rootName), outputContext.getElement(rootName), modifyStructure, contextProcessRequirement);
					//matchers when merge back to context variable
					for(String matchPath :matchers.keySet()) {
						out.addOutputMatchers(new HAPContextPath(new HAPContextDefinitionRootId(rootName), matchPath).getFullPath(), HAPMatcherUtility.reversMatchers(matchers.get(matchPath)));
					}
				}
			}
		}
		else if(output instanceof HAPContextGroup) {
			HAPContextGroup targetContextGroup = (HAPContextGroup)output;
			HAPContext outputContext = out.getContext().getContext();
			for(String rootName : outputContext.getElementNames()) {
				//merge back to context variable
				if(targetContextGroup.getElement(new HAPContextDefinitionRootId(rootName))!=null) {
					Map<String, HAPMatchers> matchers = HAPUtilityContext.mergeContextRoot(targetContextGroup.getElement(new HAPContextDefinitionRootId(rootName)), outputContext.getElement(rootName), modifyStructure, contextProcessRequirement);
					//matchers when merge back to context variable
					for(String matchPath :matchers.keySet()) {
						out.addOutputMatchers(new HAPContextPath(new HAPContextDefinitionRootId(rootName), matchPath).getFullPath(), HAPMatcherUtility.reversMatchers(matchers.get(matchPath)));
					}
				}
			}
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

	private static void processDataAssocationContext(HAPExecutableDataAssociationGroup out, HAPContextGroup inputContextGroup, HAPDefinitionDataAssociationGroup dataAssociation, HAPRequirementContextProcessor contextProcessRequirement) {
		out.setProcessConfigure(HAPUtilityDataAssociation.getContextProcessConfigurationForProcess());
		//build context group to be processed
		HAPContextGroup daContextGroup = new HAPContextGroup();
		daContextGroup.setContext(helpCategary, HAPUtilityContext.consolidateContextRoot(dataAssociation));
		
		//process context to build context
		HAPContextGroup daContextGroupProcessed = HAPProcessorContext.process(daContextGroup, inputContextGroup, out.getProcessConfigure(), contextProcessRequirement);

		//build flat context without mapped context
		HAPContext mappedContext = daContextGroupProcessed.removeContext(helpCategary);
		HAPContextFlat flatContext = HAPUtilityContext.buildFlatContextFromContextGroup(daContextGroupProcessed, null);
		
		//then add mapped context to flat context
		for(String eleName : mappedContext.getElementNames()) {
			HAPContextDefinitionRoot rootEle = mappedContext.getElement(eleName);
			markAsMappedRoot(rootEle);
			flatContext.addElement(eleName, rootEle);
		}

		out.setContext(flatContext);
	}
	
	private static void buildPathMappingInDataAssociation(HAPExecutableDataAssociationGroup dataAssociationExe, boolean isFlatInput, boolean isFlatOutput) {
		//build path mapping according for mapped element only
		Map<String, String> pathMapping = new LinkedHashMap<String, String>();
		for(String eleName : dataAssociationExe.getContext().getContext().getElementNames()) {
			HAPContextDefinitionRoot root = dataAssociationExe.getContext().getContext().getElement(eleName);
			//only root do mapping
			if(isMappedRoot(root) && HAPConstant.UIRESOURCE_CONTEXTINFO_RELATIVECONNECTION_PHYSICAL.equals(HAPUtilityContextInfo.getRelativeConnectionValue(root.getInfo()))) {
				pathMapping.putAll(HAPUtilityDataAssociation.buildRelativePathMapping(root, buildRootNameAccordingToFlat(eleName, isFlatOutput), isFlatInput));
			}
		}
		dataAssociationExe.setPathMapping(pathMapping);
	}
	
	//if flat, aaa__bbb
	//if not flat, aaa.bbb
	private static String buildRootNameAccordingToFlat(String eleName, boolean isFlatOutput) {
		HAPContextDefinitionRootId eleId = new HAPContextDefinitionRootId(eleName);
		if(isFlatOutput)  return eleId.getFullName();
		else return eleId.getPath();
	}
}
