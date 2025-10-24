package com.nosliw.core.application.common.structure.reference;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang3.tuple.Pair;

import com.nosliw.common.exception.HAPServiceData;
import com.nosliw.common.path.HAPComplexPath;
import com.nosliw.common.path.HAPPath;
import com.nosliw.common.utils.HAPConstant;
import com.nosliw.common.utils.HAPConstantShared;
import com.nosliw.core.application.HAPBundle;
import com.nosliw.core.application.common.datadefinition.HAPDataDefinitionWritable;
import com.nosliw.core.application.common.datadefinition.HAPDefinitionDataRule;
import com.nosliw.core.application.common.structure.HAPElementStructure;
import com.nosliw.core.application.common.structure.HAPElementStructureLeafData;
import com.nosliw.core.application.common.structure.HAPElementStructureLeafRelative;
import com.nosliw.core.application.common.structure.HAPElementStructureLeafRelativeForDefinition;
import com.nosliw.core.application.common.structure.HAPElementStructureLeafRelativeForValue;
import com.nosliw.core.application.common.structure.HAPElementStructureNode;
import com.nosliw.core.application.common.structure.HAPInfoElement;
import com.nosliw.core.application.common.structure.HAPInfoPathToSolidRoot;
import com.nosliw.core.application.common.structure.HAPProcessorStructureElement;
import com.nosliw.core.application.common.structure.HAPRootInStructure;
import com.nosliw.core.application.common.structure.HAPStructure;
import com.nosliw.core.application.common.structure.HAPUtilityElement;
import com.nosliw.core.application.valueport.HAPIdValuePortInBundle;
import com.nosliw.core.application.valueport.HAPReferenceElement;
import com.nosliw.core.application.valueport.HAPResultReferenceResolve;
import com.nosliw.core.application.valueport.HAPUtilityResovleElement;
import com.nosliw.core.data.HAPDataTypeHelper;
import com.nosliw.core.data.matcher.HAPMatcherUtility;
import com.nosliw.core.data.matcher.HAPMatchers;
import com.nosliw.core.resource.HAPManagerResource;
import com.nosliw.core.runtime.HAPRuntimeInfo;

public class HAPUtilityProcessRelativeElementInBundle {

	public static void processRelativeInStructure(HAPStructure valueStructure, HAPConfigureProcessorRelative processRelativeConfigure, Set<HAPIdValuePortInBundle>  dependency, List<HAPServiceData> errors, HAPBundle bundle, HAPManagerResource resourceMan, HAPDataTypeHelper dataTypeHelper, HAPRuntimeInfo runtimeInfo) {
		if(processRelativeConfigure==null) {
			processRelativeConfigure = new HAPConfigureProcessorRelative();
		} 
		
		for(HAPRootInStructure rootStructure : valueStructure.getRoots().values()) {
			HAPElementStructure rootElement = processRelativeInStructureElement(new HAPInfoElement(rootStructure.getDefinition(), new HAPComplexPath(rootStructure.getName())), processRelativeConfigure, dependency, errors, bundle, resourceMan, dataTypeHelper, runtimeInfo);
			rootStructure.setDefinition(rootElement);
		}
	}

	private static HAPElementStructure processRelativeInStructureElement(HAPInfoElement structureEleInfo, HAPConfigureProcessorRelative relativeEleProcessConfigure, Set<HAPIdValuePortInBundle>  dependency, List<HAPServiceData> errors, HAPBundle bundle, HAPManagerResource resourceMan, HAPDataTypeHelper dataTypeHelper, HAPRuntimeInfo runtimeInfo) {
		HAPElementStructure defStructureElement = structureEleInfo.getElement();
		HAPElementStructure out = defStructureElement;
		switch(defStructureElement.getType()) {
		case HAPConstantShared.CONTEXT_ELEMENTTYPE_RELATIVE_FOR_DEFINITION:
		{
			HAPElementStructureLeafRelativeForDefinition relativeStructureElement = (HAPElementStructureLeafRelativeForDefinition)defStructureElement;
			if(dependency!=null) {
				dependency.add(relativeStructureElement.getReference().getValuePortId());
			}
			if(!relativeStructureElement.isProcessed()){
				out = processRelativeStructureElement((HAPElementStructureLeafRelative)structureEleInfo.getElement(), relativeEleProcessConfigure, errors, bundle, resourceMan, runtimeInfo);
			}
			break;
		}
		case HAPConstantShared.CONTEXT_ELEMENTTYPE_RELATIVE_FOR_VALUE:
		{
			HAPElementStructureLeafRelativeForValue relativeStructureElement = (HAPElementStructureLeafRelativeForValue)defStructureElement;
			if(dependency!=null) {
				dependency.add(relativeStructureElement.getReference().getValuePortId());
			}
			if(!relativeStructureElement.isProcessed()){
				out = processRelativeStructureElement((HAPElementStructureLeafRelative)structureEleInfo.getElement(), relativeEleProcessConfigure, errors, bundle, resourceMan, runtimeInfo);
				out = processRelativeStructureElementForValue((HAPElementStructureLeafRelativeForValue)out, relativeEleProcessConfigure, errors, dataTypeHelper);
			}
			break;
		}
		case HAPConstantShared.CONTEXT_ELEMENTTYPE_NODE:
		{
			Map<String, HAPElementStructure> processedChildren = new LinkedHashMap<String, HAPElementStructure>();
			HAPElementStructureNode nodeStructureElement = (HAPElementStructureNode)defStructureElement;
			for(String childName : nodeStructureElement.getChildren().keySet()) { 	
				processedChildren.put(childName, processRelativeInStructureElement(new HAPInfoElement(nodeStructureElement.getChild(childName), structureEleInfo.getElementPath().appendSegment(childName)), relativeEleProcessConfigure, dependency, errors, bundle, resourceMan, dataTypeHelper, runtimeInfo));
			}
			nodeStructureElement.setChildren(processedChildren);
			break;
		}
		}
		return out;
	}
	
	private static HAPElementStructure processRelativeStructureElement(HAPElementStructureLeafRelative defStructureElementRelative, HAPConfigureProcessorRelative relativeEleProcessConfigure, List<HAPServiceData> errors, HAPBundle bundle, HAPManagerResource resourceMan, HAPRuntimeInfo runtimeInfo){
		HAPElementStructureLeafRelative out = defStructureElementRelative;
		
		HAPReferenceElement pathReference = defStructureElementRelative.getReference();
//		HAPResultReferenceResolve resolveInfo = HAPUtilityProcessRelativeElementInBundle.analyzeElementReference(pathReference, relativeEleProcessConfigure==null?null:relativeEleProcessConfigure.getResolveStructureElementReferenceConfigure(), processContext);
		HAPResultReferenceResolve resolveInfo = HAPUtilityResovleElement.analyzeElementReferenceInBundle(pathReference, null, bundle, resourceMan, runtimeInfo);
		
		if(resolveInfo==null) {
			errors.add(HAPServiceData.createFailureData(defStructureElementRelative, HAPConstant.ERROR_PROCESSCONTEXT_NOREFFEREDNODE));
			if(!relativeEleProcessConfigure.isTolerantNoParentForRelative()) {
				throw new RuntimeException();
			}
		}
		else {
			resolveInfo.finalElement = HAPUtilityResolveReference.resolveFinalElement(resolveInfo.elementInfoSolid, relativeEleProcessConfigure.isInheritRule());
			if(resolveInfo.finalElement!=null) {
				//refer to solid
				if(relativeEleProcessConfigure.isTrackingToSolid()) {
					String refRootId = null;
					String refPath = null;
					HAPElementStructure parentContextEle = resolveInfo.elementInfoOriginal.resolvedElement; 
//					if(parentContextEle.getType().equals(HAPConstantShared.CONTEXT_ELEMENTTYPE_RELATIVE)) {
//						HAPInfoPathToSolidRoot parentSolidNodeRef = ((HAPElementStructureLeafRelative)parentContextEle).getSolidNodeReference();
//						refRootId = parentSolidNodeRef.getRootNodeId();
//						refPath = HAPUtilityNamingConversion.cascadePath(parentSolidNodeRef.getPath().getPath(), resolveInfo.elementInfoSolid.remainPath.getPath());
//					}
//					else {
//						refRootId = resolveInfo.referredRoot.getId();
//						refPath = HAPUtilityNamingConversion.cascadePath(resolveInfo.elementInfoSolid.solvedPath.getPath(), resolveInfo.elementInfoSolid.remainPath.getPath());
//					}
					defStructureElementRelative.setSolidNodeReference(new HAPInfoPathToSolidRoot(refRootId, new HAPPath(refPath)));
				}
			}
			else {
				//not find parent node, throw exception
				errors.add(HAPServiceData.createFailureData(defStructureElementRelative, HAPConstant.ERROR_PROCESSCONTEXT_NOREFFEREDNODE));
				if(!relativeEleProcessConfigure.isTolerantNoParentForRelative()) {
					throw new RuntimeException();
				}
			}
			
			out.setResolvedInfo(new HAPInfoRelativeResolve(resolveInfo.structureId, new HAPComplexPath(resolveInfo.rootName, resolveInfo.elementInfoSolid.solvedPath), resolveInfo.elementInfoSolid.remainPath, resolveInfo.finalElement));
		}
		return out;
	}
	


	
	private static HAPElementStructure processRelativeStructureElementForValue(HAPElementStructureLeafRelativeForValue defStructureElementRelative, HAPConfigureProcessorRelative relativeEleProcessConfigure, List<HAPServiceData> errors, HAPDataTypeHelper dataTypeHelper){
		HAPElementStructure out = defStructureElementRelative;

		HAPElementStructure resolvedSolidElement = defStructureElementRelative.getResolveInfo().getSolidElement();
		HAPElementStructure relativeContextEle = defStructureElementRelative.getDefinition();
		if(relativeContextEle==null) {
			defStructureElementRelative.setDefinition(resolvedSolidElement);
		}
		else {
			//figure out matchers
			List<HAPPathElementMapping> mappingPaths = new ArrayList<HAPPathElementMapping>();
			HAPUtilityElement.mergeElement(defStructureElementRelative.getResolveInfo().getSolidElement(), relativeContextEle, false, mappingPaths, null, dataTypeHelper);
			//remove all the void matchers
			Map<String, HAPMatchers> noVoidMatchers = new LinkedHashMap<String, HAPMatchers>();
			for(HAPPathElementMapping mappingPath1 : mappingPaths){
				HAPPathElementMappingVariableToVariable mappingPath = (HAPPathElementMappingVariableToVariable)mappingPath1; 
				HAPMatchers match = mappingPath.getMatcher();
				if(!match.isVoid()){
					noVoidMatchers.put(mappingPath.getPath(), match);
				}
			}
			defStructureElementRelative.setMatchers(noVoidMatchers);
			
			//inherit rule from parent
			if(relativeEleProcessConfigure.isInheritRule()) {
				HAPElementStructure solidParent = resolvedSolidElement.getSolidStructureElement();
				if(solidParent.getType().equals(HAPConstantShared.CONTEXT_ELEMENTTYPE_DATA)) {
					HAPDataDefinitionWritable solidParentDataInfo = ((HAPElementStructureLeafData)solidParent).getDataDefinition();
					HAPMatchers ruleMatchers = HAPMatcherUtility.reversMatchers(HAPMatcherUtility.cascadeMatchers(solidParentDataInfo.getRuleMatchers()==null?null:solidParentDataInfo.getRuleMatchers().getReverseMatchers(), noVoidMatchers.get("")));

					for(HAPDefinitionDataRule ruleDef : solidParentDataInfo.getRules()) {
						HAPDataDefinitionWritable relativeDataInfo = ((HAPElementStructureLeafData)relativeContextEle).getDataDefinition();
						relativeDataInfo.addRule(ruleDef);
						relativeDataInfo.setRuleMatchers(ruleMatchers, solidParentDataInfo.getRuleCriteria());
					}
				}
			}
		}
		return out;
	}
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	public static void processRelativeInStructure(HAPStructure valueStructure, HAPStructure structureFromParent, HAPConfigureProcessorRelative processRelativeConfigure, Set<HAPIdValuePortInBundle>  dependency, List<HAPServiceData> errors, HAPRuntimeEnvironment runtimeEnv) {
		if(processRelativeConfigure==null) {
			processRelativeConfigure = new HAPConfigureProcessorRelative();
		} 
		
		Map<String, HAPRootInStructure> roots = valueStructure.getRoots();
		for(String rootName : roots.keySet()) {
			HAPUtilityElement.traverseElement(roots.get(rootName).getDefinition(), null, new HAPProcessorStructureElement() {

				@Override
				public Pair<Boolean, HAPElementStructure> process(HAPInfoElement eleInfo, Object value) {
					HAPElementStructure ele = eleInfo.getElement();
					String eleType = ele.getType();
					if(eleType.equals(HAPConstantShared.CONTEXT_ELEMENTTYPE_RELATIVE_FOR_VALUE)) {
						HAPElementStructureLeafRelativeForValue forValueEle = (HAPElementStructureLeafRelativeForValue)ele;
						HAPResultReferenceResolve resolveInfo = HAPUtilityResolveReference.analyzeElementReference(forValueEle.getReference().getElementPath(), structureFromParent, null);
						resolveInfo.finalElement = HAPUtilityResolveReference.resolveFinalElement(resolveInfo.elementInfoSolid, false);
						forValueEle.setResolvedInfo(new HAPInfoRelativeResolve(resolveInfo.structureId, new HAPComplexPath(resolveInfo.rootName, resolveInfo.elementInfoSolid.solvedPath), resolveInfo.elementInfoSolid.remainPath, resolveInfo.finalElement));
						
						
					}
					else if(eleType.equals(HAPConstantShared.CONTEXT_ELEMENTTYPE_RELATIVE_FOR_DEFINITION)) {
						HAPElementStructureLeafRelativeForDefinition relativeEle = (HAPElementStructureLeafRelativeForDefinition)ele;
						HAPResultReferenceResolve resolveInfo = HAPUtilityResolveReference.analyzeElementReference(relativeEle.getReference().getElementPath(), structureFromParent, null);
						resolveInfo.finalElement = HAPUtilityResolveReference.resolveFinalElement(resolveInfo.elementInfoSolid, false);
						relativeEle.setResolvedInfo(new HAPInfoRelativeResolve(resolveInfo.structureId, new HAPComplexPath(resolveInfo.rootName, resolveInfo.elementInfoSolid.solvedPath), resolveInfo.elementInfoSolid.remainPath, resolveInfo.finalElement));
					}
					
					
					return null;
				}

				@Override
				public void postProcess(HAPInfoElement eleInfo, Object value) {
				}
				
			}, null);
		}
		
	}
	
	private static HAPElementStructure processRelativeStructureElement(HAPElementStructureLeafRelative defStructureElementRelative, HAPConfigureProcessorRelative relativeEleProcessConfigure, List<HAPServiceData> errors, HAPRuntimeEnvironment runtimeEnv){
		HAPElementStructureLeafRelative out = defStructureElementRelative;
		
		HAPReferenceElement pathReference = defStructureElementRelative.getReference();
//		HAPResultReferenceResolve resolveInfo = HAPUtilityProcessRelativeElementInBundle.analyzeElementReference(pathReference, relativeEleProcessConfigure==null?null:relativeEleProcessConfigure.getResolveStructureElementReferenceConfigure(), processContext);
		HAPResultReferenceResolve resolveInfo = analyzeElementReference(pathReference, null, runtimeEnv.getResourceManager(), runtimeEnv.getRuntime().getRuntimeInfo());
		
		if(resolveInfo==null) {
			errors.add(HAPServiceData.createFailureData(defStructureElementRelative, HAPConstant.ERROR_PROCESSCONTEXT_NOREFFEREDNODE));
			if(!relativeEleProcessConfigure.isTolerantNoParentForRelative()) {
				throw new RuntimeException();
			}
		}
		else {
			resolveInfo.finalElement = HAPUtilityResolveReference.resolveFinalElement(resolveInfo.elementInfoSolid, relativeEleProcessConfigure.isInheritRule());
			if(resolveInfo.finalElement!=null) {
				//refer to solid
				if(relativeEleProcessConfigure.isTrackingToSolid()) {
					String refRootId = null;
					String refPath = null;
					HAPElementStructure parentContextEle = resolveInfo.elementInfoOriginal.resolvedElement; 
//					if(parentContextEle.getType().equals(HAPConstantShared.CONTEXT_ELEMENTTYPE_RELATIVE)) {
//						HAPInfoPathToSolidRoot parentSolidNodeRef = ((HAPElementStructureLeafRelative)parentContextEle).getSolidNodeReference();
//						refRootId = parentSolidNodeRef.getRootNodeId();
//						refPath = HAPUtilityNamingConversion.cascadePath(parentSolidNodeRef.getPath().getPath(), resolveInfo.elementInfoSolid.remainPath.getPath());
//					}
//					else {
//						refRootId = resolveInfo.referredRoot.getId();
//						refPath = HAPUtilityNamingConversion.cascadePath(resolveInfo.elementInfoSolid.solvedPath.getPath(), resolveInfo.elementInfoSolid.remainPath.getPath());
//					}
					defStructureElementRelative.setSolidNodeReference(new HAPInfoPathToSolidRoot(refRootId, new HAPPath(refPath)));
				}
			}
			else {
				//not find parent node, throw exception
				errors.add(HAPServiceData.createFailureData(defStructureElementRelative, HAPConstant.ERROR_PROCESSCONTEXT_NOREFFEREDNODE));
				if(!relativeEleProcessConfigure.isTolerantNoParentForRelative()) {
					throw new RuntimeException();
				}
			}
			
			out.setResolvedInfo(new HAPInfoRelativeResolve(resolveInfo.structureId, new HAPComplexPath(resolveInfo.rootName, resolveInfo.elementInfoSolid.solvedPath), resolveInfo.elementInfoSolid.remainPath, resolveInfo.finalElement));
		}
		return out;
	}
	
	
	
}
