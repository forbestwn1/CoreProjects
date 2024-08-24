package com.nosliw.core.application.division.manual.common.dataassociation.mapping;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang3.tuple.Pair;

import com.nosliw.common.exception.HAPServiceData;
import com.nosliw.common.info.HAPInfo;
import com.nosliw.common.path.HAPComplexPath;
import com.nosliw.common.path.HAPPath;
import com.nosliw.common.utils.HAPConstant;
import com.nosliw.common.utils.HAPConstantShared;
import com.nosliw.core.application.HAPBundle;
import com.nosliw.core.application.common.dataassociation.HAPDataAssociationMapping;
import com.nosliw.core.application.common.dataassociation.HAPTunnel;
import com.nosliw.core.application.common.structure.HAPElementStructure;
import com.nosliw.core.application.common.structure.HAPElementStructureLeafProvide;
import com.nosliw.core.application.common.structure.HAPElementStructureLeafRelative;
import com.nosliw.core.application.common.structure.HAPElementStructureLeafRelativeForMapping;
import com.nosliw.core.application.common.structure.HAPElementStructureNode;
import com.nosliw.core.application.common.structure.HAPInfoElement;
import com.nosliw.core.application.common.structure.HAPInfoRelativeResolve;
import com.nosliw.core.application.common.structure.HAPProcessorStructureElement;
import com.nosliw.core.application.common.structure.HAPRootStructure;
import com.nosliw.core.application.common.structure.HAPUtilityStructure;
import com.nosliw.core.application.common.structure.reference.HAPProcessorElementRelative;
import com.nosliw.core.application.common.structure.reference.HAPUtilityProcessRelativeElement;
import com.nosliw.core.application.common.valueport.HAPIdRootElement;
import com.nosliw.core.application.common.valueport.HAPIdValuePortInBundle;
import com.nosliw.core.application.common.valueport.HAPReferenceElement;
import com.nosliw.core.application.common.valueport.HAPReferenceRootElement;
import com.nosliw.core.application.common.valueport.HAPResultReferenceResolve;
import com.nosliw.core.application.common.valueport.HAPUtilityStructureElementReference;
import com.nosliw.core.application.common.valueport.HAPUtilityValuePort;
import com.nosliw.core.application.common.valueport.HAPValueStructureInValuePort11111;
import com.nosliw.data.core.dataassociation.HAPUtilityDAProcess;
import com.nosliw.data.core.domain.HAPRefIdEntity;
import com.nosliw.data.core.domain.entity.HAPContextProcessor;
import com.nosliw.data.core.domain.entity.HAPExecutableEntity;
import com.nosliw.data.core.domain.valuecontext.HAPConfigureProcessorRelative;
import com.nosliw.data.core.domain.valuecontext.HAPConfigureProcessorValueStructure;
import com.nosliw.data.core.matcher.HAPMatcherUtility;
import com.nosliw.data.core.matcher.HAPMatchers;
import com.nosliw.data.core.resource.HAPManagerResource;
import com.nosliw.data.core.runtime.HAPRuntimeEnvironment;
import com.nosliw.data.core.runtime.HAPRuntimeInfo;
import com.nosliw.data.core.structure.temp.HAPUtilityContextInfo;

public class HAPManualProcessorDataAssociationMapping {

	public static HAPDataAssociationMapping processValueMapping(
			HAPManualDataAssociationMapping daDef,
			HAPPath baseBlockPath, 
			HAPPath secondBlockPath,
			HAPBundle currentBundle, 
			HAPRuntimeEnvironment runtimeEnv) 
	{
		HAPDataAssociationMapping out = new HAPDataAssociationMapping();
		
		List<HAPManualItemValueMapping> mappingItems = daDef.getItems();
		for(HAPManualItemValueMapping mappingItem : mappingItems) {
			normalizeValuePortId(mappingItem, baseBlockPath, secondBlockPath, daDef.getDirection(), currentBundle, runtimeEnv.getResourceManager(), runtimeEnv.getRuntime().getRuntimeInfo());
		
			normalizeValuePortRelativeBrickPath(mappingItem, baseBlockPath);
			
			//process out reference (root name)
			HAPReferenceRootElement targetRef = mappingItem.getTarget();
			HAPIdRootElement targetRootEleId = HAPUtilityStructureElementReference.resolveRootReferenceInBundle(targetRef, null, currentBundle, runtimeEnv.getResourceManager(), runtimeEnv.getRuntime().getRuntimeInfo());
			 
			//process in reference (relative elements)
			HAPElementStructure processedItem = processElementStructure(mappingItem.getDefinition(), new HAPConfigureProcessorRelative(), baseBlockPath, null, null, currentBundle, runtimeEnv.getResourceManager(), runtimeEnv.getRuntime().getRuntimeInfo());
			
			List<HAPTunnel> tunnels = HAPManualUtilityDataAssociationMapping.buildRelativePathMapping(targetRootEleId, processedItem, currentBundle, runtimeEnv);
			for(HAPTunnel tunnel : tunnels) {
				out.addTunnel(tunnel);
			}
		}
		return out;
	}	
	
	private static HAPElementStructure processElementStructure(HAPElementStructure defStructureElement, HAPConfigureProcessorRelative relativeEleProcessConfigure, HAPPath baseBlockPath, Set<HAPIdValuePortInBundle>  dependency, List<HAPServiceData> errors, HAPBundle currentBundle, HAPManagerResource resourceMan, HAPRuntimeInfo runtimeInfo) {
		HAPElementStructure out = defStructureElement;
		switch(defStructureElement.getType()) {
		case HAPConstantShared.CONTEXT_ELEMENTTYPE_RELATIVE_FOR_MAPPING:
		{
			HAPElementStructureLeafRelativeForMapping relativeStructureElement = (HAPElementStructureLeafRelativeForMapping)defStructureElement;
			if(dependency!=null) {
				dependency.add(relativeStructureElement.getReference().getValuePortId());
			}
			if(!relativeStructureElement.isProcessed()){
				HAPElementStructureLeafRelative defStructureElementRelative = (HAPElementStructureLeafRelative)defStructureElement;
				HAPReferenceElement pathReference = defStructureElementRelative.getReference();
//				pathReference.setValuePortId(HAPUtilityValuePort.normalizeInBundleValuePortId(pathReference.getValuePortId(), HAPConstantShared.IO_DIRECTION_OUT, baseBlockPath, currentBundle, resourceMan, runtimeInfo));
				
				HAPResultReferenceResolve resolveInfo = HAPUtilityStructureElementReference.analyzeElementReferenceInBundle(pathReference, relativeEleProcessConfigure.getResolveStructureElementReferenceConfigure(), currentBundle, resourceMan, runtimeInfo);
				
				if(resolveInfo==null) {
					errors.add(HAPServiceData.createFailureData(defStructureElement, HAPConstant.ERROR_PROCESSCONTEXT_NOREFFEREDNODE));
					if(!relativeEleProcessConfigure.isTolerantNoParentForRelative()) {
						throw new RuntimeException();
					}
				}
				else {
					resolveInfo.finalElement = HAPUtilityProcessRelativeElement.resolveFinalElement(resolveInfo.elementInfoSolid, false);
				}
				relativeStructureElement.setResolvedInfo(new HAPInfoRelativeResolve(resolveInfo.structureId, new HAPComplexPath(resolveInfo.rootName, resolveInfo.elementInfoSolid.solvedPath), resolveInfo.elementInfoSolid.remainPath, resolveInfo.finalElement));
			}
			break;
		}
		case HAPConstantShared.CONTEXT_ELEMENTTYPE_NODE:
		{
			Map<String, HAPElementStructure> processedChildren = new LinkedHashMap<String, HAPElementStructure>();
			HAPElementStructureNode nodeStructureElement = (HAPElementStructureNode)defStructureElement;
			for(String childName : nodeStructureElement.getChildren().keySet()) { 	
				processedChildren.put(childName, processElementStructure(nodeStructureElement.getChild(childName), relativeEleProcessConfigure, baseBlockPath, dependency, errors, currentBundle, resourceMan, runtimeInfo));
			}
			nodeStructureElement.setChildren(processedChildren);
			break;
		}
		}
		return out;
	}
	
	private static void normalizeValuePortRelativeBrickPath(HAPManualItemValueMapping mappingItem, HAPPath baseBlockPath) {

		HAPReferenceRootElement targetRef = mappingItem.getTarget();

		HAPUtilityValuePort.normalizeValuePortRelativeBrickPath(targetRef.getValuePortId(), baseBlockPath);
		
		HAPUtilityStructure.traverseElement(mappingItem.getDefinition(), null, new HAPProcessorStructureElement() {

			@Override
			public Pair<Boolean, HAPElementStructure> process(HAPInfoElement eleInfo, Object value) {
				if(eleInfo.getElement().getType().equals(HAPConstantShared.CONTEXT_ELEMENTTYPE_RELATIVE_FOR_MAPPING)) {
					HAPElementStructureLeafRelativeForMapping mappingEle = (HAPElementStructureLeafRelativeForMapping)eleInfo.getElement();
					HAPUtilityValuePort.normalizeValuePortRelativeBrickPath(mappingEle.getReference().getValuePortId(), baseBlockPath);
				}
				return null;
			}

			@Override
			public void postProcess(HAPInfoElement eleInfo, Object value) {	}
		}, targetRef);
		
	}
	
	private static void normalizeValuePortId(HAPManualItemValueMapping mappingItem, HAPPath baseBlockPath, HAPPath secondBlockPath, String direction, HAPBundle currentBundle, HAPManagerResource resourceMan, HAPRuntimeInfo runtimeInfo) {
		HAPReferenceRootElement targetRef = mappingItem.getTarget();
		
		final HAPPath in;
		HAPPath out = null;
		
		if(HAPConstantShared.DATAASSOCIATION_DIRECTION_UPSTREAM.equals(direction)) {
			out = secondBlockPath;
			in = baseBlockPath;
		}
		else {
			in = secondBlockPath;
			out = baseBlockPath;
		}
		
		normalizeRootReference(targetRef, HAPConstantShared.IO_DIRECTION_IN, out, currentBundle, resourceMan, runtimeInfo);
		
		HAPUtilityStructure.traverseElement(mappingItem.getDefinition(), null, new HAPProcessorStructureElement() {

			@Override
			public Pair<Boolean, HAPElementStructure> process(HAPInfoElement eleInfo, Object value) {
				if(eleInfo.getElement().getType().equals(HAPConstantShared.CONTEXT_ELEMENTTYPE_RELATIVE_FOR_MAPPING)) {
					HAPElementStructureLeafRelativeForMapping mappingEle = (HAPElementStructureLeafRelativeForMapping)eleInfo.getElement();
					normalizeRootReference(mappingEle.getReference(), HAPConstantShared.IO_DIRECTION_OUT, in, currentBundle, resourceMan, runtimeInfo);
				}
				return null;
			}

			@Override
			public void postProcess(HAPInfoElement eleInfo, Object value) {	}
		}, targetRef);
	}
	
	private static void normalizeRootReference(HAPReferenceRootElement rootRef, String ioDirection, HAPPath blockPathFromRoot, HAPBundle currentBundle, HAPManagerResource resourceMan, HAPRuntimeInfo runtimeInfo) {
		rootRef.setValuePortId(HAPUtilityValuePort.normalizeInBundleValuePortId(rootRef.getValuePortId(), ioDirection, blockPathFromRoot, currentBundle, resourceMan, runtimeInfo));
	}
	

	
	
	
	
	
	public static void processValueMapping(
			HAPDataAssociationMapping out,
			HAPExecutableEntity fromEntityExe,
			HAPContextProcessor fromProcessorContext,
			HAPDefinitionDataAssociationMapping valueMapping,
			HAPExecutableEntity toEntityExe,
			HAPContextProcessor toProcessorContext,
			HAPRuntimeEnvironment runtimeEnv
			) {
		List<HAPManualItemValueMapping> mappingItems = valueMapping.getItems();
		for(HAPManualItemValueMapping mappingItem : mappingItems) {
			
			normalizeValuePortId(mappingItem, fromEntityExe, toEntityExe);
			
			HAPReferenceRootElement targetRef = mappingItem.getTarget();
			//process out reference (root name)
			HAPReferenceRootElement targetRootEleId = HAPUtilityStructureElementReference.resolveValueStructureRootReference(targetRef, toProcessorContext);
			
			//process in reference (relative elements)
			HAPElementStructure processedItem = processElementStructure(mappingItem.getDefinition(), null, null, null, fromProcessorContext);
			HAPManualItemValueMapping<HAPReferenceRootElement> valueMappingItem = new HAPManualItemValueMapping<HAPReferenceRootElement>(processedItem, targetRootEleId);
			out.addDataExpression(valueMappingItem);
			
			//build relative assignment path mapping according to relative node
			out.addRelativePathMappings(HAPUtilityDataAssociation.buildRelativePathMapping(valueMappingItem, toProcessorContext));
			
			//build constant assignment
			
		}

		//build relative path in value port id ref
		buildValuePortEntityRelativePath(valueMapping.getBaseEntityIdPath(), out);
		
		//from and to entity
		collectRelatedEntity(out);
	}

	private static void buildValuePortEntityRelativePath(String baseEntityIdPath, HAPDataAssociationMapping mapping) {
		for(HAPTunnel valueMappingPath : mapping.getTunnels()) {
			HAPRefIdEntity fromEntityIdRef = valueMappingPath.getFromValuePortRef().getBrickId();
			String fromEntityRelativePath = buildRelativePath(baseEntityIdPath, fromEntityIdRef.getIdPath());
			fromEntityIdRef.setRelativePath(fromEntityRelativePath);
			
			HAPRefIdEntity toEntityIdRef = valueMappingPath.getToValuePortRef().getBrickId();
			String toEntityRelativePath = buildRelativePath(baseEntityIdPath, toEntityIdRef.getIdPath());
			toEntityIdRef.setRelativePath(toEntityRelativePath);
		}
	}
	
	public static void main(String[] args) {
		System.out.println(buildRelativePath("a.b.c", "a.b.c.d.e"));
	}
	
	private static void collectRelatedEntity(HAPDataAssociationMapping mapping) {
		for(HAPTunnel valueMappingPath : mapping.getTunnels()) {
			mapping.addFromEntity(valueMappingPath.getFromValuePortRef().getBrickId().getIdPath());
			mapping.addToEntity(valueMappingPath.getToValuePortRef().getBrickId().getIdPath());
		}
	}
	
	
	private static void collectProvide(HAPDataAssociationMapping mapping,  HAPElementStructure root) {
		HAPUtilityStructure.traverseElement(root, null, new HAPProcessorStructureElement() {
			@Override
			public Pair<Boolean, HAPElementStructure> process(HAPInfoElement eleInfo, Object value) {
				if(eleInfo.getElement().getType().equals(HAPConstantShared.CONTEXT_ELEMENTTYPE_PROVIDE)) {
					HAPElementStructureLeafProvide provideEle = (HAPElementStructureLeafProvide)eleInfo.getElement();
					mapping.addProvide(provideEle.getName(), provideEle.getDefinition());
					return Pair.of(false, null);
				}
				return null;
			}

			@Override
			public void postProcess(HAPInfoElement eleInfo, Object value) {	}}, null);
		
	}
	

	
	
	
	
	
	
	
	
	
	
	public static HAPDataAssociationMapping processDataAssociation(HAPContainerStructure input, HAPDefinitionDataAssociationMapping dataAssociation, HAPContainerStructure output, HAPInfo daProcessConfigure, HAPRuntimeEnvironment runtimeEnv) {
		HAPDataAssociationMapping out = new HAPDataAssociationMapping(dataAssociation, input, output);
		processDataAssociation(out, input, dataAssociation, output, daProcessConfigure, runtimeEnv);
		return out;
	}
	
	//process input configure for activity and generate flat context for activity
	public static void processDataAssociation(HAPDataAssociationMapping out, HAPContainerStructure input, HAPDefinitionDataAssociationMapping dataAssociation, HAPContainerStructure output, HAPInfo daProcessConfigure, HAPRuntimeEnvironment runtimeEnv) {
		Map<String, HAPDefinitionValueMapping> valueMappings = dataAssociation.getMappings();
		for(String targetName : valueMappings.keySet()) {
			HAPExecutableValueMapping associationExe = processValueMapping(input, valueMappings.get(targetName), output.getStructure(targetName), out.getInputDependency(), daProcessConfigure, runtimeEnv);
			out.addMapping(targetName, associationExe);
		}
	}

	public static void enhanceDataAssociationEndPointContext(HAPContainerStructure input, boolean inputEnhance, HAPDefinitionDataAssociationMapping dataAssociation, HAPContainerStructure output, boolean outputEnhance, HAPRuntimeEnvironment runtimeEnv) {
		Map<String, HAPDefinitionValueMapping> associations = dataAssociation.getMappings();
		for(String targetName : associations.keySet()) {
			enhanceAssociationEndPointContext(input, inputEnhance, associations.get(targetName), output.getStructure(targetName), outputEnhance, runtimeEnv);
		}
	}
	
	//enhance input and output context according to dataassociation
	public static void enhanceAssociationEndPointContext(HAPContainerStructure input, boolean inputEnhance, HAPDefinitionValueMapping associationDef, HAPValueStructureInValuePort11111 outputStructure, boolean outputEnhance, HAPRuntimeEnvironment runtimeEnv) {
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
					if(sourceContextEle==null) {
						throw new RuntimeException();
					}
					//update input: set referred element defined in output to input
					HAPElementStructureLeafRelative relativeEle = (HAPElementStructureLeafRelative)contextEleInfo.getElement();
					HAPElementStructure solidateSourceContextEle = sourceContextEle.getSolidStructureElement();
					if(solidateSourceContextEle==null) {
						throw new RuntimeException();
					}
					HAPUtilityStructure.setDescendantByNamePath(input.getStructure(relativeEle.getReference().getParentValueContextName()), new HAPComplexPath(relativeEle.getReference().getPath()), solidateSourceContextEle.cloneStructureElement());
				} else {
					throw new RuntimeException();
				}
			}
		}
		
		//try to enhance output context
		if(outputEnhance) {
			for(String eleName : mappingItems.keySet()) {
				HAPUtilityStructure.traverseElement(mappingItems.get(eleName), eleName, new HAPProcessorStructureElement() {

					@Override
					public Pair<Boolean, HAPElementStructure> process(HAPInfoElement eleInfo, Object value) {
						HAPValueStructureInValuePort11111 outputStructure = (HAPValueStructureInValuePort11111)value;
						if(eleInfo.getElement().getType().equals(HAPConstantShared.CONTEXT_ELEMENTTYPE_RELATIVE)) {
							//only relative element
							HAPElementStructureLeafRelative relativeEle = (HAPElementStructureLeafRelative)eleInfo.getElement();
							//if element path exist in output structure
							HAPResultReferenceResolve targetResolvedInfo = HAPUtilityStructureElementReference.resolveElementReference(eleInfo.getElementPath().getFullName(), outputStructure, processConfigure.elementReferenceResolveMode, processConfigure.relativeInheritRule, null);
							if(!HAPUtilityStructureElementReference.isLogicallySolved(targetResolvedInfo)) {
								//target node in output according to path not exist
								//element in input structure
								HAPValueStructureInValuePort11111 sourceContextStructure = input.getStructure(relativeEle.getReference().getParentValueContextName());
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
								} else {
									throw new RuntimeException();
								}
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
	
	private static HAPDefinitionValueMapping updateOutputNameWithId(HAPContainerStructure input, HAPDefinitionValueMapping valueMapping, HAPValueStructureInValuePort11111 outputStructure) {
		HAPDefinitionValueMapping out = new HAPDefinitionValueMapping();
		Map<String, HAPRootStructure> items = valueMapping.getItems();
		for(String target : items.keySet()) {
			HAPRootStructure mapping = items.get(target);
			HAPRootStructure targetRoot = HAPUtilityStructure.getRootByName(target, outputStructure);
			out.addItem(targetRoot.getLocalId(), items.get(target));
		}
		return out;
	}
	
	
	public static HAPExecutableValueMapping processValueMapping(HAPContainerStructure input, HAPDefinitionValueMapping valueMapping, HAPValueStructureInValuePort11111 outputStructure, Set<String> parentDependency, HAPInfo daProcessConfigure, HAPRuntimeEnvironment runtimeEnv) {
		HAPExecutableValueMapping out = new HAPExecutableValueMapping();

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
	private static Map<String, String> buildRelativePathMappingInDataAssociation1(HAPDefinitionValueMapping valueMapping) {
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

	private static Map<String, Object> buildConstantAssignmentInDataAssociation(HAPDefinitionValueMapping valueMapping) {
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
