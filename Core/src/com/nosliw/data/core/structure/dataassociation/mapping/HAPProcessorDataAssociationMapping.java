package com.nosliw.data.core.structure.dataassociation.mapping;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang3.tuple.Pair;

import com.nosliw.common.exception.HAPServiceData;
import com.nosliw.common.info.HAPInfo;
import com.nosliw.common.utils.HAPConstant;
import com.nosliw.common.utils.HAPConstantShared;
import com.nosliw.data.core.component.attachment.HAPContainerAttachment;
import com.nosliw.data.core.matcher.HAPMatcherUtility;
import com.nosliw.data.core.matcher.HAPMatchers;
import com.nosliw.data.core.runtime.HAPRuntimeEnvironment;
import com.nosliw.data.core.structure.HAPConfigureProcessorStructure;
import com.nosliw.data.core.structure.HAPElement;
import com.nosliw.data.core.structure.HAPElementLeafRelative;
import com.nosliw.data.core.structure.HAPIdContextDefinitionRoot;
import com.nosliw.data.core.structure.HAPInfoReferenceResolve;
import com.nosliw.data.core.structure.HAPInfoElement;
import com.nosliw.data.core.structure.HAPReferenceElement;
import com.nosliw.data.core.structure.HAPProcessorContext;
import com.nosliw.data.core.structure.HAPProcessorContextDefinitionElement;
import com.nosliw.data.core.structure.HAPRoot;
import com.nosliw.data.core.structure.HAPUtilityContext;
import com.nosliw.data.core.structure.HAPUtilityContextInfo;
import com.nosliw.data.core.structure.dataassociation.HAPUtilityDAProcess;
import com.nosliw.data.core.structure.story.HAPParentContext;
import com.nosliw.data.core.structure.value.HAPStructureValueDefinition;
import com.nosliw.data.core.structure.value.HAPStructureValueDefinitionFlat;
import com.nosliw.data.core.structure.value.HAPStructureValueDefinitionGroup;

public class HAPProcessorDataAssociationMapping {

	public static HAPExecutableDataAssociationMapping processDataAssociation(HAPParentContext input, HAPDefinitionDataAssociationMapping dataAssociation, HAPParentContext output, HAPContainerAttachment attachmentContainer, HAPInfo daProcessConfigure, HAPRuntimeEnvironment runtimeEnv) {
		HAPExecutableDataAssociationMapping out = new HAPExecutableDataAssociationMapping(dataAssociation, input);
		processDataAssociation(out, input, dataAssociation, output, attachmentContainer, daProcessConfigure, runtimeEnv);
		return out;
	}
	
	//process input configure for activity and generate flat context for activity
	public static void processDataAssociation(HAPExecutableDataAssociationMapping out, HAPParentContext input, HAPDefinitionDataAssociationMapping dataAssociation, HAPParentContext output, HAPContainerAttachment attachmentContainer, HAPInfo daProcessConfigure, HAPRuntimeEnvironment runtimeEnv) {
		Map<String, HAPStructureValueDefinitionFlat> associations = dataAssociation.getAssociations();
		for(String targetName : associations.keySet()) {
			HAPExecutableAssociation associationExe = processAssociation(input, associations.get(targetName), output.getContext(targetName), attachmentContainer, out.getInputDependency(), daProcessConfigure, runtimeEnv);
			out.addAssociation(targetName, associationExe);
		}
	}

	public static void enhanceDataAssociationEndPointContext(HAPParentContext input, boolean inputEnhance, HAPDefinitionDataAssociationMapping dataAssociation, HAPParentContext output, boolean outputEnhance, HAPRuntimeEnvironment runtimeEnv) {
		Map<String, HAPStructureValueDefinitionFlat> associations = dataAssociation.getAssociations();
		for(String targetName : associations.keySet()) {
			enhanceAssociationEndPointContext(input, inputEnhance, associations.get(targetName), output.getContext(targetName), outputEnhance, runtimeEnv);
		}
	}
	
	//enhance input and output context according to dataassociation
	private static void enhanceAssociationEndPointContext(HAPParentContext input, boolean inputEnhance, HAPStructureValueDefinitionFlat associationDef, HAPStructureValueDefinition outputStructure, boolean outputEnhance, HAPRuntimeEnvironment runtimeEnv) {
		associationDef = normalizeOutputNameInDataAssociation(input, associationDef, outputStructure);
		HAPInfo info = HAPUtilityDAProcess.withModifyInputStructureConfigure(null, inputEnhance);
		info = HAPUtilityDAProcess.withModifyOutputStructureConfigure(info, outputEnhance);
		HAPConfigureProcessorStructure processConfigure = HAPUtilityDataAssociation.getContextProcessConfigurationForDataAssociation(info);
		List<HAPServiceData> errors = new ArrayList<HAPServiceData>();

		//process data association definition in order to find missing context data definition from input
		HAPStructureValueDefinitionFlat daContextProcessed = HAPProcessorContext.process(associationDef, input, null, null, errors, processConfigure, runtimeEnv);
		
		//try to enhance input context according to error
		if(inputEnhance) {
			for(HAPServiceData error : errors) {
				String errorMsg = error.getMessage();
				if(HAPConstant.ERROR_PROCESSCONTEXT_NOREFFEREDNODE.equals(errorMsg)) {
					//enhance input context according to error
					HAPInfoElement contextEleInfo = (HAPInfoElement)error.getData();
					//find referred element defined in output
					HAPReferenceElement path = contextEleInfo.getElementPath();
					HAPElement sourceContextEle = HAPUtilityContext.getDescendant(outputStructure.getRoot(path.getRootStructureId().getName(), false).getDefinition(), path.getSubPath());
					if(sourceContextEle==null)  throw new RuntimeException();
					//update input: set referred element defined in output to input
					HAPElementLeafRelative relativeEle = (HAPElementLeafRelative)contextEleInfo.getElement();
					HAPElement solidateSourceContextEle = sourceContextEle.getSolidStructureElement();
					if(solidateSourceContextEle==null)    throw new RuntimeException();
					HAPUtilityContext.setDescendant(input.getContext(relativeEle.getParent()), relativeEle.getPathFormat(), solidateSourceContextEle.cloneStructureElement());
				}
				else  throw new RuntimeException();
			}
		}
		
		//try to enhance output context
		if(outputEnhance) {
			for(String eleName : associationDef.getRootNames()) {
				HAPUtilityContext.processContextRootElement(associationDef.getRoot(eleName), eleName, new HAPProcessorContextDefinitionElement() {

					@Override
					public Pair<Boolean, HAPElement> process(HAPInfoElement eleInfo, Object value) {
						HAPStructureValueDefinition outputStructure = (HAPStructureValueDefinition)value;
						if(eleInfo.getElement().getType().equals(HAPConstantShared.CONTEXT_ELEMENTTYPE_RELATIVE)) {
							//only relative element
							HAPElementLeafRelative relativeEle = (HAPElementLeafRelative)eleInfo.getElement();
							//if element path exist in output structure
							HAPInfoReferenceResolve targetResolvedInfo = HAPUtilityContext.resolveReferencedContextElement(eleInfo.getElementPath(), outputStructure);
							if(!HAPUtilityContext.isLogicallySolved(targetResolvedInfo)) {
								//target node in output according to path not exist
								//element in input structure
								HAPStructureValueDefinition sourceContextStructure = input.getContext(relativeEle.getParent());
								HAPInfoReferenceResolve sourceResolvedInfo = HAPUtilityContext.resolveReferencedContextElement(relativeEle.getPathFormat(), sourceContextStructure);
								if(HAPUtilityContext.isLogicallySolved(sourceResolvedInfo)) {
									HAPElement sourceEle = sourceResolvedInfo.resolvedNode;
									if(sourceEle.getType().equals(HAPConstantShared.CONTEXT_ELEMENTTYPE_DATA)) {
										HAPUtilityContext.setDescendant(outputStructure, eleInfo.getElementPath(), sourceEle.getSolidStructureElement());
									}
									else if(sourceEle.getType().equals(HAPConstantShared.CONTEXT_ELEMENTTYPE_VALUE)) {
										
									}
									else if(sourceEle.getType().equals(HAPConstantShared.CONTEXT_ELEMENTTYPE_CONSTANT)) {
										
									}
								}
								else  throw new RuntimeException();
							}
						}
						return null;
					}

					@Override
					public void postProcess(HAPInfoElement eleInfo, Object value) {  }
				}, outputStructure);
			}			
		}		
	}
	
	//make output name in da to be global name according to refference to outputStrucutre
	private static HAPStructureValueDefinitionFlat normalizeOutputNameInDataAssociation(HAPParentContext input, HAPStructureValueDefinitionFlat associationDef, HAPStructureValueDefinition outputStructure) {
		HAPStructureValueDefinitionFlat out = associationDef;
		if(outputStructure instanceof HAPStructureValueDefinitionGroup) {
			//for output context group only
			//find refereed context in output
			//update root name with full name (containing categary and element name)
			HAPStructureValueDefinitionFlat origin = associationDef;
			out = new HAPStructureValueDefinitionFlat();
			for(String eleName : origin.getRootNames()) {
				String updatedName = eleName;
				HAPInfoReferenceResolve resolvedInfo = HAPUtilityContext.resolveReferencedContextElement(new HAPReferenceElement(eleName), (HAPStructureValueDefinitionGroup)outputStructure, null, null);
				if(resolvedInfo!=null) 	updatedName = resolvedInfo.path.getRootStructureId().getFullName();
				out.addRoot(updatedName, origin.getRoot(eleName));
			}
		}
		return out;
	}

	private static HAPExecutableAssociation processAssociation(HAPParentContext input, HAPStructureValueDefinitionFlat associationDef, HAPStructureValueDefinition outputStructure, HAPContainerAttachment attachmentContainer, Set<String> parentDependency, HAPInfo daProcessConfigure, HAPRuntimeEnvironment runtimeEnv) {
		HAPExecutableAssociation out = new HAPExecutableAssociation(input, associationDef, outputStructure);

		associationDef = normalizeOutputNameInDataAssociation(input, associationDef, outputStructure);
		
		//process mapping
		List<HAPServiceData> errors = new ArrayList<HAPServiceData>();
		HAPConfigureProcessorStructure processConfigure = HAPUtilityDataAssociation.getContextProcessConfigurationForDataAssociation(daProcessConfigure);
		HAPStructureValueDefinitionFlat daContextProcessed = HAPProcessorContext.process(associationDef, input, attachmentContainer, parentDependency, errors, processConfigure, runtimeEnv);
		out.setMapping(daContextProcessed);
		buildRelativePathMappingInDataAssociation(out);
		buildConstantAssignmentInDataAssociation(out);

		//matchers to output
		switch(outputStructure.getType()) {
		case HAPConstantShared.CONTEXTSTRUCTURE_TYPE_FLAT:
		case HAPConstantShared.CONTEXTSTRUCTURE_TYPE_NOTFLAT:
			HAPStructureValueDefinitionFlat mapping = out.getMapping();
			for(String rootName : mapping.getRootNames()) {
				//merge back to context variable
				if(outputStructure.getRoot(rootName, false)!=null) {
					Map<String, HAPMatchers> matchers = HAPUtilityContext.mergeRoot(outputStructure.getRoot(rootName, false), mapping.getRoot(rootName), HAPUtilityDAProcess.ifModifyOutputStructure(daProcessConfigure), runtimeEnv);
					//matchers when merge back to context variable
					for(String matchPath :matchers.keySet()) {
						out.addOutputMatchers(new HAPReferenceElement(new HAPIdContextDefinitionRoot(rootName), matchPath).getFullPath(), HAPMatcherUtility.reversMatchers(matchers.get(matchPath)));
					}
				}
			}
			break;
		}

		return out;
	}

	//build assignment path mapping according to relative node
	private static void buildRelativePathMappingInDataAssociation(HAPExecutableAssociation dataAssociationExe) {
		//build path mapping according for mapped element only
		Map<String, String> pathMapping = new LinkedHashMap<String, String>();
		for(String eleName : dataAssociationExe.getMapping().getRootNames()) {
			HAPRoot root = dataAssociationExe.getMapping().getRoot(eleName);
			//only physical root do mapping
			if(HAPConstantShared.UIRESOURCE_CONTEXTINFO_RELATIVECONNECTION_PHYSICAL.equals(HAPUtilityContextInfo.getRelativeConnectionValue(root.getInfo()))) {
				pathMapping.putAll(HAPUtilityDataAssociation.buildRelativePathMapping(root, buildRootNameAccordingToFlat(eleName, dataAssociationExe.isFlatOutput()), dataAssociationExe.isFlatInput()));
			}
		}
		dataAssociationExe.setRelativePathMappings(pathMapping);
	}

	private static void buildConstantAssignmentInDataAssociation(HAPExecutableAssociation dataAssociationExe) {
		//build path mapping according for mapped element only
		Map<String, Object> constantAssignment = new LinkedHashMap<String, Object>();
		for(String eleName : dataAssociationExe.getMapping().getRootNames()) {
			HAPRoot root = dataAssociationExe.getMapping().getRoot(eleName);
			//only physical root do mapping
			if(HAPConstantShared.UIRESOURCE_CONTEXTINFO_RELATIVECONNECTION_PHYSICAL.equals(HAPUtilityContextInfo.getRelativeConnectionValue(root.getInfo()))) {
				constantAssignment.putAll(HAPUtilityDataAssociation.buildConstantAssignment(root, buildRootNameAccordingToFlat(eleName, dataAssociationExe.isFlatOutput()), dataAssociationExe.isFlatInput()));
			}
		}
		dataAssociationExe.setConstantAssignments(constantAssignment);
	}

	
	//if flat, aaa__bbb
	//if not flat, aaa.bbb
	private static String buildRootNameAccordingToFlat(String eleName, boolean isFlatOutput) {
		HAPIdContextDefinitionRoot eleId = new HAPIdContextDefinitionRoot(eleName);
		if(isFlatOutput)  return eleId.getFullName();
		else return eleId.getPathFormat();
	}
}
