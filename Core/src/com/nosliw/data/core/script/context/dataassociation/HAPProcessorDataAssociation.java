package com.nosliw.data.core.script.context.dataassociation;

import java.util.LinkedHashMap;
import java.util.Map;

import com.nosliw.common.utils.HAPConstant;
import com.nosliw.data.core.matcher.HAPMatcherUtility;
import com.nosliw.data.core.matcher.HAPMatchers;
import com.nosliw.data.core.script.context.HAPConfigureContextProcessor;
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

	public static HAPExecutableDataAssociation processDataAssociation(HAPParentContext input, HAPDefinitionDataAssociation dataAssociation, HAPParentContext output, HAPConfigureDataAssociationProcessor daProcessConfigure, HAPRequirementContextProcessor contextProcessRequirement) {
		HAPExecutableDataAssociation out = new HAPExecutableDataAssociation(dataAssociation);
		processDataAssociation(input, out, output, daProcessConfigure==null?new HAPConfigureDataAssociationProcessor():daProcessConfigure, contextProcessRequirement);
		return out;
	}
	
	//process input configure for activity and generate flat context for activity
	public static void processDataAssociation(HAPParentContext input, HAPExecutableDataAssociation out, HAPParentContext output, HAPConfigureDataAssociationProcessor daProcessConfigure, HAPRequirementContextProcessor contextProcessRequirement) {
		out.setProcessConfigure(HAPUtilityDataAssociation.getContextProcessConfigurationForProcess());
		HAPDefinitionDataAssociation dataAssociation = out.getDefinition();
		Map<String, HAPContext> associations = dataAssociation.getAssociations();
		for(String targetName : associations.keySet()) {
			HAPExecutableAssociation associationExe = processAssociation(input, associations.get(targetName), output.getContext(targetName), daProcessConfigure==null?new HAPConfigureDataAssociationProcessor():daProcessConfigure, out.getProcessConfigure(), contextProcessRequirement);
			out.addAssociation(targetName, associationExe);
		}
	}
	
	private static HAPExecutableAssociation processAssociation(HAPParentContext input, HAPContext associationDef, HAPContextStructure outputStructure, HAPConfigureDataAssociationProcessor daProcessConfigure, HAPConfigureContextProcessor processConfigure, HAPRequirementContextProcessor contextProcessRequirement) {
		HAPExecutableAssociation out = new HAPExecutableAssociation(input, associationDef, outputStructure);

		if(outputStructure instanceof HAPContextGroup) {
			//update root name with full name (containing categary and element name)
			HAPContext origin = associationDef;
			HAPContext updated = new HAPContext();
			for(String eleName : origin.getElementNames()) {
				String updatedName = eleName;
				HAPInfoRelativeContextResolve resolvedInfo = HAPUtilityContext.resolveReferencedParentContextNode(new HAPContextPath(eleName), (HAPContextGroup)outputStructure, null, null);
				if(resolvedInfo!=null) 	updatedName = resolvedInfo.path.getRootElementId().getFullName();
				updated.addElement(updatedName, origin.getElement(eleName));
			}
			associationDef = updated;
		}
		
		//process mapping
		HAPContext daContextProcessed = HAPProcessorContext.process(associationDef, input, processConfigure, contextProcessRequirement);
		out.setMapping(daContextProcessed);
		buildPathMappingInDataAssociation(out);

		//matchers to output
		switch(outputStructure.getType()) {
		case HAPConstant.CONTEXTSTRUCTURE_TYPE_FLAT:
		case HAPConstant.CONTEXTSTRUCTURE_TYPE_NOTFLAT:
			HAPContext mapping = out.getMapping();
			for(String rootName : mapping.getElementNames()) {
				//merge back to context variable
				if(outputStructure.getElement(rootName)!=null) {
					Map<String, HAPMatchers> matchers = HAPUtilityContext.mergeContextRoot(outputStructure.getElement(rootName), mapping.getElement(rootName), daProcessConfigure.modifyStructure, contextProcessRequirement);
					//matchers when merge back to context variable
					for(String matchPath :matchers.keySet()) {
						out.addOutputMatchers(new HAPContextPath(new HAPContextDefinitionRootId(rootName), matchPath).getFullPath(), HAPMatcherUtility.reversMatchers(matchers.get(matchPath)));
					}
				}
			}
			break;
		}

		return out;
	}

	private static void buildPathMappingInDataAssociation(HAPExecutableAssociation dataAssociationExe) {
		//build path mapping according for mapped element only
		Map<String, String> pathMapping = new LinkedHashMap<String, String>();
		for(String eleName : dataAssociationExe.getMapping().getElementNames()) {
			HAPContextDefinitionRoot root = dataAssociationExe.getMapping().getElement(eleName);
			//only physical root do mapping
			if(HAPConstant.UIRESOURCE_CONTEXTINFO_RELATIVECONNECTION_PHYSICAL.equals(HAPUtilityContextInfo.getRelativeConnectionValue(root.getInfo()))) {
				pathMapping.putAll(HAPUtilityDataAssociation.buildRelativePathMapping(root, buildRootNameAccordingToFlat(eleName, dataAssociationExe.isFlatOutput()), dataAssociationExe.isFlatInput()));
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
