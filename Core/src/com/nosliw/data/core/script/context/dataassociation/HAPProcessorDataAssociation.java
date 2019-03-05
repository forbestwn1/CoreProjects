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
import com.nosliw.data.core.script.context.HAPParentContext;
import com.nosliw.data.core.script.context.HAPProcessorContext;
import com.nosliw.data.core.script.context.HAPRequirementContextProcessor;
import com.nosliw.data.core.script.context.HAPUtilityContext;
import com.nosliw.data.core.script.context.HAPUtilityContextInfo;

public class HAPProcessorDataAssociation {

	private static String INFO_MAPPEDROOT = "mappedRoot";

	private static String helpCategary = HAPConstant.UIRESOURCE_CONTEXTTYPE_PRIVATE;

	public static HAPExecutableDataAssociation processDataAssociation(HAPDataAssociationIO input, HAPDefinitionDataAssociation dataAssociation, boolean isFlatOutput, HAPRequirementContextProcessor contextProcessRequirement) {
		HAPExecutableDataAssociation out = new HAPExecutableDataAssociation(dataAssociation);
		processDataAssociation(input, out, isFlatOutput, contextProcessRequirement);
		return out;
	}

	public static void processDataAssociation(HAPDataAssociationIO input, HAPExecutableDataAssociation out, boolean isFlatOutput, HAPRequirementContextProcessor contextProcessRequirement) {
		if(input instanceof HAPContext)  processDataAssociation((HAPContext)input, out, isFlatOutput, contextProcessRequirement);
		if(input instanceof HAPContextGroup)  processDataAssociation((HAPContextGroup)input, out, isFlatOutput, contextProcessRequirement);
	}
	
	public static HAPExecutableDataAssociation processDataAssociation(HAPContextGroup inputContextGroup, HAPDefinitionDataAssociation dataAssociation, boolean isFlatOutput, HAPRequirementContextProcessor contextProcessRequirement) {
		HAPExecutableDataAssociation out = new HAPExecutableDataAssociation(dataAssociation);
		processDataAssociation(inputContextGroup, out, isFlatOutput, contextProcessRequirement);
		return out;
	}

	public static HAPExecutableDataAssociation processDataAssociation(HAPContext inputContext, HAPDefinitionDataAssociation dataAssociation, boolean isFlatOutput, HAPRequirementContextProcessor contextProcessRequirement) {
		HAPExecutableDataAssociation out = new HAPExecutableDataAssociation(dataAssociation);
		processDataAssociation(inputContext, out, isFlatOutput, contextProcessRequirement);
		return out;
	}
	
	//process input configure for activity and generate flat context for activity
	public static void processDataAssociation(HAPContextGroup inputContextGroup, HAPExecutableDataAssociation out, boolean isFlatOutput, HAPRequirementContextProcessor contextProcessRequirement) {
		HAPDefinitionDataAssociation dataAssociation = out.getDefinition();
		processDataAssocationContext(out, inputContextGroup, dataAssociation, isFlatOutput, contextProcessRequirement);
		buildPathMappingInDataAssociation(out, out.isFlatInput(), isFlatOutput);
	}

	public static void processDataAssociation(HAPContext inputContext, HAPExecutableDataAssociation out, boolean isFlatOutput, HAPRequirementContextProcessor contextProcessRequirement) {
		HAPDefinitionDataAssociation dataAssociation = out.getDefinition();
		//build input context group
		HAPContextGroup inputContextGroup = new HAPContextGroup();
		inputContextGroup.setContext(HAPConstant.UIRESOURCE_CONTEXTTYPE_PUBLIC, inputContext.cloneContext());

		processDataAssocationContext(out, inputContextGroup, dataAssociation, isFlatOutput, contextProcessRequirement);

		HAPContext flatContext = out.getContext();
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

	public static HAPExecutableDataAssociationWithTarget processDataAssociation(HAPDataAssociationIO input, HAPDefinitionDataAssociation dataAssociation, HAPDataAssociationIO output, boolean modifyStructure, HAPRequirementContextProcessor contextProcessRequirement) {
		HAPExecutableDataAssociationWithTarget out = new HAPExecutableDataAssociationWithTarget(dataAssociation);
		processDataAssociation(input, out, output, modifyStructure, contextProcessRequirement);
		return out;
	}
	
	public static void processDataAssociation(HAPDataAssociationIO input, HAPExecutableDataAssociationWithTarget out, HAPDataAssociationIO output, boolean modifyStructure, HAPRequirementContextProcessor contextProcessRequirement) {
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
			HAPContext outputContext = out.getContextFlat().getContext();
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

	private static void processDataAssocationContext(HAPExecutableDataAssociation out, HAPContextGroup inputContextGroup, HAPDefinitionDataAssociation dataAssociation, boolean isFlatOutput, HAPRequirementContextProcessor contextProcessRequirement) {
		out.setIsFlatOutput(isFlatOutput);
		out.setProcessConfigure(HAPUtilityDataAssociation.getContextProcessConfigurationForProcess());
		//build context group to be processed
		HAPContextGroup daContextGroup = new HAPContextGroup();
		daContextGroup.setContext(helpCategary, HAPUtilityContext.consolidateContextRoot(dataAssociation));
		
		//process context to build context
		HAPContextGroup daContextGroupProcessed = HAPProcessorContext.process(daContextGroup, HAPParentContext.createDefault(inputContextGroup), out.getProcessConfigure(), contextProcessRequirement);

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
	
	private static void buildPathMappingInDataAssociation(HAPExecutableDataAssociation dataAssociationExe, boolean isFlatInput, boolean isFlatOutput) {
		//build path mapping according for mapped element only
		Map<String, String> pathMapping = new LinkedHashMap<String, String>();
		for(String eleName : dataAssociationExe.getContext().getElementNames()) {
			HAPContextDefinitionRoot root = dataAssociationExe.getContextFlat().getContext().getElement(eleName);
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
