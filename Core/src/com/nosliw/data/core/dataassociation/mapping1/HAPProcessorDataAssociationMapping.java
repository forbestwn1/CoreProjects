package com.nosliw.data.core.dataassociation.mapping1;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang3.tuple.Pair;

import com.nosliw.common.exception.HAPServiceData;
import com.nosliw.common.info.HAPInfo;
import com.nosliw.common.path.HAPComplexPath;
import com.nosliw.common.utils.HAPConstant;
import com.nosliw.common.utils.HAPConstantShared;
import com.nosliw.core.application.common.structure.HAPElementStructure;
import com.nosliw.core.application.common.structure.HAPElementStructureLeafRelative;
import com.nosliw.core.application.common.structure.HAPInfoElement;
import com.nosliw.core.application.common.structure.HAPProcessorStructureElement;
import com.nosliw.core.application.common.structure.HAPUtilityStructure;
import com.nosliw.core.application.common.structure.reference.HAPProcessorElementRelative;
import com.nosliw.core.application.common.valueport.HAPResultReferenceResolve;
import com.nosliw.core.application.common.valueport.HAPUtilityStructureElementReference;
import com.nosliw.core.application.valuestructure.HAPRootStructure;
import com.nosliw.data.core.dataassociation.HAPUtilityDAProcess;
import com.nosliw.data.core.domain.valuecontext.HAPConfigureProcessorValueStructure;
import com.nosliw.data.core.matcher.HAPMatcherUtility;
import com.nosliw.data.core.matcher.HAPMatchers;
import com.nosliw.data.core.runtime.HAPRuntimeEnvironment;
import com.nosliw.data.core.structure.temp.HAPUtilityContextInfo;
import com.nosliw.data.core.valuestructure1.HAPContainerStructure;
import com.nosliw.data.core.valuestructure1.HAPValueStructure;

public class HAPProcessorDataAssociationMapping {

	public static HAPExecutableDataAssociationMapping processDataAssociation(HAPContainerStructure input, HAPDefinitionDataAssociationMapping dataAssociation, HAPContainerStructure output, HAPInfo daProcessConfigure, HAPRuntimeEnvironment runtimeEnv) {
		HAPExecutableDataAssociationMapping out = new HAPExecutableDataAssociationMapping(dataAssociation, input, output);
		processDataAssociation(out, input, dataAssociation, output, daProcessConfigure, runtimeEnv);
		return out;
	}
	
	//process input configure for activity and generate flat context for activity
	public static void processDataAssociation(HAPExecutableDataAssociationMapping out, HAPContainerStructure input, HAPDefinitionDataAssociationMapping dataAssociation, HAPContainerStructure output, HAPInfo daProcessConfigure, HAPRuntimeEnvironment runtimeEnv) {
		Map<String, HAPValueMapping> valueMappings = dataAssociation.getMappings();
		for(String targetName : valueMappings.keySet()) {
			HAPExecutableMapping associationExe = processValueMapping(input, valueMappings.get(targetName), output.getStructure(targetName), out.getInputDependency(), daProcessConfigure, runtimeEnv);
			out.addMapping(targetName, associationExe);
		}
	}

	public static void enhanceDataAssociationEndPointContext(HAPContainerStructure input, boolean inputEnhance, HAPDefinitionDataAssociationMapping dataAssociation, HAPContainerStructure output, boolean outputEnhance, HAPRuntimeEnvironment runtimeEnv) {
		Map<String, HAPValueMapping> associations = dataAssociation.getMappings();
		for(String targetName : associations.keySet()) {
			enhanceAssociationEndPointContext(input, inputEnhance, associations.get(targetName), output.getStructure(targetName), outputEnhance, runtimeEnv);
		}
	}
	
	//enhance input and output context according to dataassociation
	public static void enhanceAssociationEndPointContext(HAPContainerStructure input, boolean inputEnhance, HAPValueMapping associationDef, HAPValueStructureInValuePort outputStructure, boolean outputEnhance, HAPRuntimeEnvironment runtimeEnv) {
		HAPInfo info = HAPUtilityDAProcess.withModifyInputStructureConfigure(null, inputEnhance);
		info = HAPUtilityDAProcess.withModifyOutputStructureConfigure(info, outputEnhance);
		HAPConfigureProcessorValueStructure processConfigure = HAPUtilityDataAssociation.getContextProcessConfigurationForDataAssociation(info);
		List<HAPServiceData> errors = new ArrayList<HAPServiceData>();

		//process data association definition in order to find missing context data definition from input
		Map<String, HAPRootStructure> mappingItems = associationDef.getItems();
		for(String targetId : mappingItems.keySet()) {
			HAPRootStructure item1 = HAPProcessorElementRelative.process(mappingItems.get(targetId), targetId, input, null, errors, processConfigure, runtimeEnv);
		}

		//try to enhance input context according to error
		if(inputEnhance) {
			for(HAPServiceData error : errors) {
				String errorMsg = error.getMessage();
				if(HAPConstant.ERROR_PROCESSCONTEXT_NOREFFEREDNODE.equals(errorMsg)) {
					//enhance input context according to error
					HAPInfoElement contextEleInfo = (HAPInfoElement)error.getData();
					//find referred element defined in output
					HAPComplexPath path = contextEleInfo.getElementPath();
					HAPElementStructure sourceContextEle = HAPUtilityStructure.getDescendant(outputStructure.getRoot(path.getRoot()).getDefinition(), path.getPathStr());
					if(sourceContextEle==null)  throw new RuntimeException();
					//update input: set referred element defined in output to input
					HAPElementStructureLeafRelative relativeEle = (HAPElementStructureLeafRelative)contextEleInfo.getElement();
					HAPElementStructure solidateSourceContextEle = sourceContextEle.getSolidStructureElement();
					if(solidateSourceContextEle==null)    throw new RuntimeException();
					HAPUtilityStructure.setDescendantByNamePath(input.getStructure(relativeEle.getReference().getParentValueContextName()), new HAPComplexPath(relativeEle.getReference().getPath()), solidateSourceContextEle.cloneStructureElement());
				}
				else  throw new RuntimeException();
			}
		}
		
		//try to enhance output context
		if(outputEnhance) {
			for(String eleName : mappingItems.keySet()) {
				HAPUtilityStructure.traverseElement(mappingItems.get(eleName), eleName, new HAPProcessorStructureElement() {

					@Override
					public Pair<Boolean, HAPElementStructure> process(HAPInfoElement eleInfo, Object value) {
						HAPValueStructureInValuePort outputStructure = (HAPValueStructureInValuePort)value;
						if(eleInfo.getElement().getType().equals(HAPConstantShared.CONTEXT_ELEMENTTYPE_RELATIVE)) {
							//only relative element
							HAPElementStructureLeafRelative relativeEle = (HAPElementStructureLeafRelative)eleInfo.getElement();
							//if element path exist in output structure
							HAPResultReferenceResolve targetResolvedInfo = HAPUtilityStructureElementReference.resolveElementReference(eleInfo.getElementPath().getFullName(), outputStructure, processConfigure.elementReferenceResolveMode, processConfigure.relativeInheritRule, null);
							if(!HAPUtilityStructureElementReference.isLogicallySolved(targetResolvedInfo)) {
								//target node in output according to path not exist
								//element in input structure
								HAPValueStructureInValuePort sourceContextStructure = input.getStructure(relativeEle.getReference().getParentValueContextName());
								HAPResultReferenceResolve sourceResolvedInfo = HAPUtilityStructureElementReference.resolveElementReference(relativeEle.getReference().getPath(), sourceContextStructure, processConfigure.elementReferenceResolveMode, processConfigure.relativeInheritRule, null);
								if(HAPUtilityStructureElementReference.isLogicallySolved(sourceResolvedInfo)) {
									HAPElementStructure sourceEle = sourceResolvedInfo.finalElement;
									if(sourceEle.getType().equals(HAPConstantShared.CONTEXT_ELEMENTTYPE_DATA)) {
										HAPUtilityStructure.setDescendantByNamePath(outputStructure, eleInfo.getElementPath(), sourceEle.getSolidStructureElement());
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
	
	private static HAPValueMapping updateOutputNameWithId(HAPContainerStructure input, HAPValueMapping valueMapping, HAPValueStructureInValuePort outputStructure) {
		HAPValueMapping out = new HAPValueMapping();
		Map<String, HAPRootStructure> items = valueMapping.getItems();
		for(String target : items.keySet()) {
			HAPRootStructure mapping = items.get(target);
			HAPRootStructure targetRoot = HAPUtilityStructure.getRootByName(target, outputStructure);
			out.addItem(targetRoot.getLocalId(), items.get(target));
		}
		return out;
	}
	
	public static HAPExecutableMapping processValueMapping(HAPContainerStructure input, HAPValueMapping valueMapping, HAPValueStructureInValuePort outputStructure, Set<String> parentDependency, HAPInfo daProcessConfigure, HAPRuntimeEnvironment runtimeEnv) {
		HAPExecutableMapping out = new HAPExecutableMapping();

		valueMapping = updateOutputNameWithId(input, valueMapping, outputStructure);
		
		//process relative
		{
			List<HAPServiceData> errors = new ArrayList<HAPServiceData>();
			HAPConfigureProcessorValueStructure processConfigure = HAPUtilityDataAssociation.getContextProcessConfigurationForDataAssociation(daProcessConfigure);
			Map<String, HAPRootStructure> mappingItems = valueMapping.getItems();
			for(String targetId : mappingItems.keySet()) {
				HAPRootStructure item1 = HAPProcessorElementRelative.process(mappingItems.get(targetId), input, parentDependency, errors, processConfigure, runtimeEnv);
				valueMapping.addItem(targetId, item1);
			}
		}
		
		out.setRelativePathMappings(buildRelativePathMappingInDataAssociation(valueMapping));

		out.setConstantAssignments(buildConstantAssignmentInDataAssociation(valueMapping));

		Map<String, HAPRootStructure> mappingItems = valueMapping.getItems();
		for(String targetId : mappingItems.keySet()) {
			//merge back to context variable
			HAPRootStructure outputRoot = outputStructure.getRoot(targetId);
			if(outputRoot!=null) {
				Map<String, HAPMatchers> matchers = HAPUtilityStructure.mergeRoot(outputRoot, mappingItems.get(targetId), HAPUtilityDAProcess.ifModifyOutputStructure(daProcessConfigure), runtimeEnv);
				//matchers when merge back to context variable
				for(String matchPath :matchers.keySet()) {
					out.addOutputMatchers(new HAPComplexPath(targetId, matchPath).getFullName(), HAPMatcherUtility.reversMatchers(matchers.get(matchPath)));
				}
			}
		}

		out.setMapping(valueMapping);
		return out;
	}

	//build assignment path mapping according to relative node
	private static Map<String, String> buildRelativePathMappingInDataAssociation(HAPValueMapping valueMapping) {
		//build path mapping according for mapped element only
		Map<String, String> pathMapping = new LinkedHashMap<String, String>();
		Map<String, HAPRootStructure> items = valueMapping.getItems();
		for(String eleName : items.keySet()) {
			HAPRootStructure root = items.get(eleName);
			//only physical root do mapping
			if(HAPConstantShared.UIRESOURCE_CONTEXTINFO_RELATIVECONNECTION_PHYSICAL.equals(HAPUtilityContextInfo.getRelativeConnectionValue(root.getInfo()))) {
				pathMapping.putAll(HAPUtilityDataAssociation.buildRelativePathMapping(root, eleName));
			}
		}
		return pathMapping;
	}

	private static Map<String, Object> buildConstantAssignmentInDataAssociation(HAPValueMapping valueMapping) {
		//build path mapping according for mapped element only
		Map<String, Object> out = new LinkedHashMap<String, Object>();
		Map<String, HAPRootStructure> items = valueMapping.getItems();
		for(String eleName : items.keySet()) {
			HAPRootStructure root = items.get(eleName);
			//only physical root do mapping
			if(HAPConstantShared.UIRESOURCE_CONTEXTINFO_RELATIVECONNECTION_PHYSICAL.equals(HAPUtilityContextInfo.getRelativeConnectionValue(root.getInfo()))) {
				out.putAll(HAPUtilityDataAssociation.buildConstantAssignment(root, eleName));
			}
		}
		return out;
	}
}
