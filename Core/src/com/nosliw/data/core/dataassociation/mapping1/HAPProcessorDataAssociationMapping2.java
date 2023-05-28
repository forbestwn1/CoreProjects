package com.nosliw.data.core.dataassociation.mapping1;

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
import com.nosliw.data.core.dataassociation.HAPUtilityDAProcess;
import com.nosliw.data.core.domain.entity.attachment.HAPDefinitionEntityContainerAttachment;
import com.nosliw.data.core.domain.entity.valuestructure.HAPRootStructure;
import com.nosliw.data.core.domain.valuecontext.HAPConfigureProcessorValueStructure;
import com.nosliw.data.core.matcher.HAPMatchers;
import com.nosliw.data.core.runtime.HAPRuntimeEnvironment;
import com.nosliw.data.core.structure.HAPElementStructure;
import com.nosliw.data.core.structure.HAPElementStructureLeafRelative;
import com.nosliw.data.core.structure.HAPInfoElement;
import com.nosliw.data.core.structure.HAPReferenceElementInStructure;
import com.nosliw.data.core.structure.reference.HAPInfoReferenceResolve;
import com.nosliw.data.core.structure.temp.HAPProcessorContext;
import com.nosliw.data.core.structure.temp.HAPProcessorContextDefinitionElement;
import com.nosliw.data.core.structure.temp.HAPUtilityContext;
import com.nosliw.data.core.structure.temp.HAPUtilityContextInfo;
import com.nosliw.data.core.valuestructure1.HAPContainerStructure;
import com.nosliw.data.core.valuestructure1.HAPValueStructure;
import com.nosliw.data.core.valuestructure1.HAPValueStructureDefinitionFlat;
import com.nosliw.data.core.valuestructure1.HAPValueStructureDefinitionGroup;

public class HAPProcessorDataAssociationMapping2 {

	public static HAPExecutableDataAssociationMapping processDataAssociation(HAPContainerStructure input, HAPDefinitionDataAssociationMapping dataAssociation, HAPContainerStructure output, HAPDefinitionEntityContainerAttachment attachmentContainer, HAPInfo daProcessConfigure, HAPRuntimeEnvironment runtimeEnv) {
		HAPExecutableDataAssociationMapping out = new HAPExecutableDataAssociationMapping(dataAssociation, input);
		processDataAssociation(out, input, dataAssociation, output, attachmentContainer, daProcessConfigure, runtimeEnv);
		return out;
	}
	
	//process input configure for activity and generate flat context for activity
	public static void processDataAssociation(HAPExecutableDataAssociationMapping out, HAPContainerStructure input, HAPDefinitionDataAssociationMapping dataAssociation, HAPContainerStructure output, HAPDefinitionEntityContainerAttachment attachmentContainer, HAPInfo daProcessConfigure, HAPRuntimeEnvironment runtimeEnv) {
		Map<String, HAPValueMapping> valueMappings = dataAssociation.getMappings();
		for(String targetName : valueMappings.keySet()) {
			HAPExecutableMapping associationExe = processAssociation(input, valueMappings.get(targetName), output.getStructure(targetName), attachmentContainer, out.getInputDependency(), daProcessConfigure, runtimeEnv);
			out.addMapping(targetName, associationExe);
		}
	}

	public static void enhanceDataAssociationEndPointContext(HAPContainerStructure input, boolean inputEnhance, HAPDefinitionDataAssociationMapping dataAssociation, HAPContainerStructure output, boolean outputEnhance, HAPRuntimeEnvironment runtimeEnv) {
		Map<String, HAPValueStructureDefinitionFlat> associations = dataAssociation.getMappings();
		for(String targetName : associations.keySet()) {
			enhanceAssociationEndPointContext(input, inputEnhance, associations.get(targetName), output.getStructure(targetName), outputEnhance, runtimeEnv);
		}
	}
	
	//enhance input and output context according to dataassociation
	private static void enhanceAssociationEndPointContext(HAPContainerStructure input, boolean inputEnhance, HAPValueStructureDefinitionFlat associationDef, HAPValueStructure outputStructure, boolean outputEnhance, HAPRuntimeEnvironment runtimeEnv) {
		associationDef = normalizeOutputNameInDataAssociation(input, associationDef, outputStructure);
		HAPInfo info = HAPUtilityDAProcess.withModifyInputStructureConfigure(null, inputEnhance);
		info = HAPUtilityDAProcess.withModifyOutputStructureConfigure(info, outputEnhance);
		HAPConfigureProcessorValueStructure processConfigure = HAPUtilityDataAssociation.getContextProcessConfigurationForDataAssociation(info);
		List<HAPServiceData> errors = new ArrayList<HAPServiceData>();

		//process data association definition in order to find missing context data definition from input
		HAPValueStructureDefinitionFlat daContextProcessed = HAPProcessorContext.process(associationDef, input, null, null, errors, processConfigure, runtimeEnv);
		
		//try to enhance input context according to error
		if(inputEnhance) {
			for(HAPServiceData error : errors) {
				String errorMsg = error.getMessage();
				if(HAPConstant.ERROR_PROCESSCONTEXT_NOREFFEREDNODE.equals(errorMsg)) {
					//enhance input context according to error
					HAPInfoElement contextEleInfo = (HAPInfoElement)error.getData();
					//find referred element defined in output
					HAPReferenceElementInStructure path = contextEleInfo.getElementPath();
					HAPElementStructure sourceContextEle = HAPUtilityContext.getDescendant(outputStructure.getRoot(path.getRootReference().getName(), false).getDefinition(), path.getSubPath());
					if(sourceContextEle==null)  throw new RuntimeException();
					//update input: set referred element defined in output to input
					HAPElementStructureLeafRelative relativeEle = (HAPElementStructureLeafRelative)contextEleInfo.getElement();
					HAPElementStructure solidateSourceContextEle = sourceContextEle.getSolidStructureElement();
					if(solidateSourceContextEle==null)    throw new RuntimeException();
					HAPUtilityContext.setDescendant(input.getStructure(relativeEle.getParentValueContextName()), relativeEle.getPathFormat(), solidateSourceContextEle.cloneStructureElement());
				}
				else  throw new RuntimeException();
			}
		}
		
		//try to enhance output context
		if(outputEnhance) {
			for(String eleName : associationDef.getRootNames()) {
				HAPUtilityContext.processContextRootElement(associationDef.getRoot(eleName), eleName, new HAPProcessorContextDefinitionElement() {

					@Override
					public Pair<Boolean, HAPElementStructure> process(HAPInfoElement eleInfo, Object value) {
						HAPValueStructure outputStructure = (HAPValueStructure)value;
						if(eleInfo.getElement().getType().equals(HAPConstantShared.CONTEXT_ELEMENTTYPE_RELATIVE)) {
							//only relative element
							HAPElementStructureLeafRelative relativeEle = (HAPElementStructureLeafRelative)eleInfo.getElement();
							//if element path exist in output structure
							HAPInfoReferenceResolve targetResolvedInfo = HAPUtilityContext.resolveReferencedContextElement(eleInfo.getElementPath(), outputStructure);
							if(!HAPUtilityContext.isLogicallySolved(targetResolvedInfo)) {
								//target node in output according to path not exist
								//element in input structure
								HAPValueStructure sourceContextStructure = input.getStructure(relativeEle.getParentValueContextName());
								HAPInfoReferenceResolve sourceResolvedInfo = HAPUtilityContext.resolveReferencedContextElement(relativeEle.getPathFormat(), sourceContextStructure);
								if(HAPUtilityContext.isLogicallySolved(sourceResolvedInfo)) {
									HAPElementStructure sourceEle = sourceResolvedInfo.finalElement;
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
	private static HAPValueStructureDefinitionFlat normalizeOutputNameInDataAssociation(HAPContainerStructure input, HAPValueStructureDefinitionFlat associationDef, HAPValueStructure outputStructure) {
		HAPValueStructureDefinitionFlat out = associationDef;
		if(outputStructure instanceof HAPValueStructureDefinitionGroup) {
			//for output context group only
			//find refereed context in output
			//update root name with full name (containing categary and element name)
			HAPValueStructureDefinitionFlat origin = associationDef;
			out = new HAPValueStructureDefinitionFlat();
			for(String eleName : origin.getRootNames()) {
				String updatedName = eleName;
				HAPInfoReferenceResolve resolvedInfo = HAPUtilityContext.analyzeElementReference(new HAPReferenceElementInStructure(eleName), (HAPValueStructureDefinitionGroup)outputStructure, null, null);
				if(resolvedInfo!=null) 	updatedName = resolvedInfo.path.getRootReference().getFullName();
				out.addRootToCategary(updatedName, origin.getRoot(eleName));
			}
		}
		return out;
	}

	private static HAPExecutableMapping processAssociation(HAPContainerStructure input, HAPValueMapping valueMapping, HAPValueStructure outputStructure, HAPDefinitionEntityContainerAttachment attachmentContainer, Set<String> parentDependency, HAPInfo daProcessConfigure, HAPRuntimeEnvironment runtimeEnv) {
		HAPExecutableMapping out = new HAPExecutableMapping(input, valueMapping, outputStructure);

		valueMapping = normalizeOutputNameInDataAssociation(input, valueMapping, outputStructure);
		
		//process mapping
		List<HAPServiceData> errors = new ArrayList<HAPServiceData>();
		HAPConfigureProcessorValueStructure processConfigure = HAPUtilityDataAssociation.getContextProcessConfigurationForDataAssociation(daProcessConfigure);
		HAPValueStructureDefinitionFlat daContextProcessed = HAPProcessorContext.process(valueMapping, input, attachmentContainer, parentDependency, errors, processConfigure, runtimeEnv);
		out.setMapping(daContextProcessed);
		buildRelativePathMappingInDataAssociation(out);
		buildConstantAssignmentInDataAssociation(out);

		//matchers to output
		switch(outputStructure.getDataType()) {
		case HAPConstantShared.CONTEXTSTRUCTURE_TYPE_FLAT:
		case HAPConstantShared.CONTEXTSTRUCTURE_TYPE_NOTFLAT:
			HAPValueStructureDefinitionFlat mapping = out.getMapping();
			for(String rootName : mapping.getRootNames()) {
				//merge back to context variable
				if(outputStructure.getRoot(rootName, false)!=null) {
					Map<String, HAPMatchers> matchers = HAPUtilityContext.mergeRoot(outputStructure.getRoot(rootName, false), mapping.getRoot(rootName), HAPUtilityDAProcess.ifModifyOutputStructure(daProcessConfigure), runtimeEnv);
					//matchers when merge back to context variable
					for(String matchPath :matchers.keySet()) {
//						out.addOutputMatchers(new HAPReferenceElement(new HAPIdContextDefinitionRoot(rootName), matchPath).getFullPath(), HAPMatcherUtility.reversMatchers(matchers.get(matchPath)));
					}
				}
			}
			break;
		}

		return out;
	}

	//build assignment path mapping according to relative node
	private static void buildRelativePathMappingInDataAssociation(HAPExecutableMapping dataAssociationExe) {
		//build path mapping according for mapped element only
		Map<String, String> pathMapping = new LinkedHashMap<String, String>();
		for(String eleName : dataAssociationExe.getMapping().getRootNames()) {
			HAPRootStructure root = dataAssociationExe.getMapping().getRoot(eleName);
			//only physical root do mapping
			if(HAPConstantShared.UIRESOURCE_CONTEXTINFO_RELATIVECONNECTION_PHYSICAL.equals(HAPUtilityContextInfo.getRelativeConnectionValue(root.getInfo()))) {
				pathMapping.putAll(HAPUtilityDataAssociation.buildRelativePathMapping(root, buildRootNameAccordingToFlat(eleName, dataAssociationExe.isFlatOutput()), dataAssociationExe.isFlatInput()));
			}
		}
		dataAssociationExe.setRelativePathMappings(pathMapping);
	}

	private static void buildConstantAssignmentInDataAssociation(HAPExecutableMapping dataAssociationExe) {
		//build path mapping according for mapped element only
		Map<String, Object> constantAssignment = new LinkedHashMap<String, Object>();
		for(String eleName : dataAssociationExe.getMapping().getRootNames()) {
			HAPRootStructure root = dataAssociationExe.getMapping().getRoot(eleName);
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
//		HAPIdContextDefinitionRoot eleId = new HAPIdContextDefinitionRoot(eleName);
		if(isFlatOutput)  return eleId.getFullName();
		else return eleId.getPathFormat();
	}
}
