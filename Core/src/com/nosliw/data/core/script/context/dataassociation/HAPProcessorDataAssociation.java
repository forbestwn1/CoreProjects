package com.nosliw.data.core.script.context.dataassociation;

import java.util.LinkedHashMap;
import java.util.Map;

import com.nosliw.common.utils.HAPConstant;
import com.nosliw.data.core.matcher.HAPMatcherUtility;
import com.nosliw.data.core.matcher.HAPMatchers;
import com.nosliw.data.core.script.context.HAPContext;
import com.nosliw.data.core.script.context.HAPContextDefinitionRoot;
import com.nosliw.data.core.script.context.HAPContextDefinitionRootId;
import com.nosliw.data.core.script.context.HAPContextGroup;
import com.nosliw.data.core.script.context.HAPContextPath;
import com.nosliw.data.core.script.context.HAPContextStructure;
import com.nosliw.data.core.script.context.HAPInfoRelativeContextResolve;
import com.nosliw.data.core.script.context.HAPParentContext;
import com.nosliw.data.core.script.context.HAPProcessorContext;
import com.nosliw.data.core.script.context.HAPRequirementContextProcessor;
import com.nosliw.data.core.script.context.HAPUtilityContext;
import com.nosliw.data.core.script.context.HAPUtilityContextInfo;

public class HAPProcessorDataAssociation {

//	private static String INFO_MAPPEDROOT = "mappedRoot";

	private static String helpCategary = HAPConstant.UIRESOURCE_CONTEXTTYPE_PRIVATE;

	public static HAPExecutableDataAssociation processDataAssociation(HAPParentContext input, HAPDefinitionDataAssociation dataAssociation, boolean isFlatOutput, HAPRequirementContextProcessor contextProcessRequirement) {
		HAPExecutableDataAssociation out = new HAPExecutableDataAssociation(dataAssociation);
		processDataAssociation(input, out, isFlatOutput, contextProcessRequirement);
		return out;
	}

	//process input configure for activity and generate flat context for activity
	public static void processDataAssociation(HAPParentContext input, HAPExecutableDataAssociation out, boolean isFlatOutput, HAPRequirementContextProcessor contextProcessRequirement) {
		HAPDefinitionDataAssociation dataAssociation = out.getDefinition();
		for(String inputName : input.getNames()) {
			out.addIsFlatInput(inputName, input.getContext(inputName, dataAssociation).isFlat());
		}
		processDataAssocationContext(out, input, dataAssociation, isFlatOutput, contextProcessRequirement);
		buildPathMappingInDataAssociation(out, out.isFlatInput(), isFlatOutput);
	}

//	public static void processDataAssociation(HAPContextStructure input, HAPExecutableDataAssociation out, boolean isFlatOutput, HAPRequirementContextProcessor contextProcessRequirement) {
//		if(input instanceof HAPContext)  processDataAssociation(input, out, isFlatOutput, contextProcessRequirement);
//		if(input instanceof HAPContextGroup)  processDataAssociation(input, out, isFlatOutput, contextProcessRequirement);
//	}
//	
//	public static HAPExecutableDataAssociation processDataAssociation(HAPContextGroup inputContextGroup, HAPDefinitionDataAssociation dataAssociation, boolean isFlatOutput, HAPRequirementContextProcessor contextProcessRequirement) {
//		HAPExecutableDataAssociation out = new HAPExecutableDataAssociation(dataAssociation);
//		processDataAssociation(inputContextGroup, out, isFlatOutput, contextProcessRequirement);
//		return out;
//	}
//
//	public static HAPExecutableDataAssociation processDataAssociation(HAPContext inputContext, HAPDefinitionDataAssociation dataAssociation, boolean isFlatOutput, HAPRequirementContextProcessor contextProcessRequirement) {
//		HAPExecutableDataAssociation out = new HAPExecutableDataAssociation(dataAssociation);
//		processDataAssociation(inputContext, out, isFlatOutput, contextProcessRequirement);
//		return out;
//	}

	public static HAPExecutableDataAssociationWithTarget processDataAssociation(HAPParentContext input, HAPDefinitionDataAssociation dataAssociation, HAPContextStructure output, boolean modifyStructure, HAPRequirementContextProcessor contextProcessRequirement) {
		HAPExecutableDataAssociationWithTarget out = new HAPExecutableDataAssociationWithTarget(dataAssociation);
		processDataAssociation(input, out, output, modifyStructure, contextProcessRequirement);
		return out;
	}
	
	public static void processDataAssociation(HAPParentContext input, HAPExecutableDataAssociationWithTarget out, HAPContextStructure output, boolean modifyStructure, HAPRequirementContextProcessor contextProcessRequirement) {
		boolean isFlatOutput = true;
		if(output instanceof HAPContext)  isFlatOutput = true;   
		else if(output instanceof HAPContextGroup)   isFlatOutput = false;
		
		if(output instanceof HAPContextGroup) {
			//update root name with full name (containing categary and element name)
			HAPDefinitionDataAssociation origin = out.getDefinition();
			HAPDefinitionDataAssociation updated = origin.cloneDataAssocationBase();
			for(String eleName : origin.getElementNames()) {
				String updatedName = eleName;
				HAPInfoRelativeContextResolve resolvedInfo = HAPUtilityContext.resolveReferencedParentContextNode(new HAPContextPath(eleName), (HAPContextGroup)output, null, null);
				if(resolvedInfo!=null) 	updatedName = resolvedInfo.path.getRootElementId().getFullName();
				updated.addElement(updatedName, origin.getElement(eleName));
			}
			out.setDefinition(updated);
		}
		
		//process data association
		processDataAssociation(input, out, isFlatOutput, contextProcessRequirement);

		//process result
		if(output instanceof HAPContext) {
			HAPContext targetContext = (HAPContext)output;
			HAPContext outputContext = out.getContext();
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
			HAPContext outputContext = out.getContext();
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
	
//	public static void markAsMappedRoot(HAPContextDefinitionRoot root) {
//		root.getInfo().setValue(INFO_MAPPEDROOT, "yes");
//	}
//
//	public static boolean isMappedRoot(HAPContextDefinitionRoot root) {
//		return root.getInfo().getValue(INFO_MAPPEDROOT)!=null;
//	}
//	
//	public static void markAsMappedContext(HAPContext context) {
//		for(String rootName : context.getElementNames()) {
//			markAsMappedRoot(context.getElement(rootName));
//		}
//	}

	private static void processDataAssocationContext(HAPExecutableDataAssociation out, HAPParentContext inputContext, HAPDefinitionDataAssociation dataAssociation, boolean isFlatOutput, HAPRequirementContextProcessor contextProcessRequirement) {
		out.setIsFlatOutput(isFlatOutput);
		out.setProcessConfigure(HAPUtilityDataAssociation.getContextProcessConfigurationForProcess());
		
		HAPContext daContextProcessed = HAPProcessorContext.process(dataAssociation, inputContext, out.getProcessConfigure(), contextProcessRequirement);
		out.setContext(daContextProcessed);
	}
	
	private static void buildPathMappingInDataAssociation(HAPExecutableDataAssociation dataAssociationExe, Map<String, Boolean> isFlatInput, boolean isFlatOutput) {
		//build path mapping according for mapped element only
		Map<String, String> pathMapping = new LinkedHashMap<String, String>();
		for(String eleName : dataAssociationExe.getContext().getElementNames()) {
			HAPContextDefinitionRoot root = dataAssociationExe.getContext().getElement(eleName);
			//only root do mapping
			if(HAPConstant.UIRESOURCE_CONTEXTINFO_RELATIVECONNECTION_PHYSICAL.equals(HAPUtilityContextInfo.getRelativeConnectionValue(root.getInfo()))) {
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
