package com.nosliw.core.application.common.structure.reference;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.nosliw.common.exception.HAPServiceData;
import com.nosliw.common.path.HAPComplexPath;
import com.nosliw.common.path.HAPPath;
import com.nosliw.common.utils.HAPConstant;
import com.nosliw.common.utils.HAPConstantShared;
import com.nosliw.common.utils.HAPUtilityBasic;
import com.nosliw.core.application.HAPBundle;
import com.nosliw.core.application.common.structure.HAPElementStructure;
import com.nosliw.core.application.common.structure.HAPElementStructureLeafData;
import com.nosliw.core.application.common.structure.HAPElementStructureLeafRelative;
import com.nosliw.core.application.common.structure.HAPElementStructureLeafRelativeForDefinition;
import com.nosliw.core.application.common.structure.HAPElementStructureLeafRelativeForValue;
import com.nosliw.core.application.common.structure.HAPElementStructureNode;
import com.nosliw.core.application.common.structure.HAPInfoElement;
import com.nosliw.core.application.common.structure.HAPInfoPathToSolidRoot;
import com.nosliw.core.application.common.structure.HAPInfoRelativeResolve;
import com.nosliw.core.application.common.structure.HAPPathElementMapping;
import com.nosliw.core.application.common.structure.HAPPathElementMappingVariableToVariable;
import com.nosliw.core.application.common.structure.HAPUtilityStructure;
import com.nosliw.core.application.common.valueport.HAPConfigureResolveElementReference;
import com.nosliw.core.application.common.valueport.HAPInfoValueStructureReference;
import com.nosliw.core.application.common.valueport.HAPReferenceValuePort;
import com.nosliw.core.application.common.valueport.HAPReferenceElement;
import com.nosliw.core.application.common.valueport.HAPResultDesendantResolve;
import com.nosliw.core.application.common.valueport.HAPResultReferenceResolve;
import com.nosliw.core.application.common.valueport.HAPUtilityStructureElementReference;
import com.nosliw.core.application.common.valueport.HAPUtilityValuePort;
import com.nosliw.core.application.common.valueport.HAPValuePort;
import com.nosliw.core.application.common.variable.HAPDataRule;
import com.nosliw.core.application.common.variable.HAPVariableDataInfo;
import com.nosliw.core.application.division.manual.brick.valuestructure.HAPManualBrickValueStructure;
import com.nosliw.core.application.valuestructure.HAPRootStructure;
import com.nosliw.data.core.data.criteria.HAPDataTypeCriteria;
import com.nosliw.data.core.data.criteria.HAPUtilityCriteria;
import com.nosliw.data.core.domain.entity.HAPContextProcessor;
import com.nosliw.data.core.domain.valuecontext.HAPConfigureProcessorRelative;
import com.nosliw.data.core.matcher.HAPMatcherUtility;
import com.nosliw.data.core.matcher.HAPMatchers;

public class HAPUtilityProcessRelativeElement {

	public static HAPResultReferenceResolve resolveElementReference(HAPReferenceElement reference, HAPPath defaultTargetBrickPathId ,HAPConfigureResolveElementReference resolveConfigure, HAPBundle bundle){
		HAPValuePort valuePort = HAPUtilityValuePort.getValuePort(reference.getValuePortRef(), defaultTargetBrickPathId, bundle);
		List<HAPInfoValueStructureReference> valueStructureInfos = valuePort.discoverCandidateValueStructure(reference.getValueStructureReference());
		
		//resolve targeted structure element
		HAPResultReferenceResolve out =  HAPUtilityStructureElementReference.analyzeElementReference(reference.getElementPath(), valueStructureInfos, resolveConfigure);
		if(out!=null) {
			out.eleReference = reference;
		}
		
		return out;
	}

	
	//resolve the remain path part
	public static HAPElementStructure resolveFinalElement(HAPResultDesendantResolve resolveInfo, Boolean relativeInheritRule) {
		HAPElementStructure out = null;
		if(relativeInheritRule==null) {
			relativeInheritRule = true;
		}
		
		if(resolveInfo.remainPath.isEmpty()) {
			//exactly match with path
			out = resolveInfo.resolvedElement.cloneStructureElement();
		}
		else {
			//nof exactly match with path
			HAPElementStructure candidateNode = resolveInfo.resolvedElement.getSolidStructureElement();
			if(HAPConstantShared.CONTEXT_ELEMENTTYPE_DATA.equals(candidateNode.getType())) {
				//data type node
				HAPElementStructureLeafData dataLeafEle = (HAPElementStructureLeafData)candidateNode;
				HAPDataTypeCriteria childCriteria = HAPUtilityCriteria.getChildCriteriaByPath(dataLeafEle.getCriteria(), resolveInfo.remainPath.getPath());
				if(childCriteria!=null) {
					out = new HAPElementStructureLeafData(new HAPVariableDataInfo(childCriteria)); 
					
					//inherit rule from parent
					if(relativeInheritRule) {
						HAPVariableDataInfo solidParentDataInfo = ((HAPElementStructureLeafData)candidateNode).getDataInfo();
						for(HAPDataRule rule : solidParentDataInfo.getRules()) {
							String subPath = null;
							String rulePath = rule.getPath();
							String remainPath = resolveInfo.remainPath.getPath();
							if(HAPUtilityBasic.isEquals(rulePath, remainPath)) {
								subPath = "";
							} else if(remainPath.contains(rulePath+".")) {
								subPath = remainPath.substring(rulePath.length()+1);
							}

							if(subPath!=null) {
								HAPDataRule newRule = rule.cloneDataRule();
								newRule.setPath(subPath);
								((HAPElementStructureLeafData)out).getDataInfo().addRule(newRule);
							}
						}
					}
					
				}
				else {
//					out.resolvedNode = new HAPContextDefinitionLeafValue();
				}
			}
			else if(HAPConstantShared.CONTEXT_ELEMENTTYPE_VALUE.equals(candidateNode.getType())){
				out = candidateNode;
			}
			else if(HAPConstantShared.CONTEXT_ELEMENTTYPE_CONSTANT.equals(candidateNode.getType())){
				//kkkkkk
				out = candidateNode;
			}
		}
		return out;
	}
	
	public static void processRelativeInStructure(HAPManualBrickValueStructure valueStructure, HAPConfigureProcessorRelative processRelativeConfigure, Set<HAPReferenceValuePort>  dependency, List<HAPServiceData> errors, HAPContextProcessor processorContext) {
		if(processRelativeConfigure==null) {
			processRelativeConfigure = new HAPConfigureProcessorRelative();
		} 
		
		for(HAPRootStructure rootStructure : valueStructure.getAllRoots()) {
			HAPElementStructure rootElement = processRelativeInStructureElement(new HAPInfoElement(rootStructure.getDefinition(), new HAPComplexPath(rootStructure.getName())), processRelativeConfigure, dependency, errors, processorContext);
			rootStructure.setDefinition(rootElement);
		}
	}

	private static HAPElementStructure processRelativeInStructureElement(HAPInfoElement structureEleInfo, HAPConfigureProcessorRelative relativeEleProcessConfigure, Set<HAPReferenceValuePort>  dependency, List<HAPServiceData> errors, HAPContextProcessor processorContext) {
		HAPElementStructure defStructureElement = structureEleInfo.getElement();
		HAPElementStructure out = defStructureElement;
		switch(defStructureElement.getType()) {
		case HAPConstantShared.CONTEXT_ELEMENTTYPE_RELATIVE_FOR_DEFINITION:
		{
			HAPElementStructureLeafRelativeForDefinition relativeStructureElement = (HAPElementStructureLeafRelativeForDefinition)defStructureElement;
			if(dependency!=null) {
				dependency.add(relativeStructureElement.getReference().getValuePortRef());
			}
			if(!relativeStructureElement.isProcessed()){
				out = processRelativeStructureElement(structureEleInfo, relativeEleProcessConfigure, errors, processorContext);
			}
			break;
		}
		case HAPConstantShared.CONTEXT_ELEMENTTYPE_RELATIVE_FOR_VALUE:
		{
			HAPElementStructureLeafRelativeForValue relativeStructureElement = (HAPElementStructureLeafRelativeForValue)defStructureElement;
			if(dependency!=null) {
				dependency.add(relativeStructureElement.getReference().getValuePortRef());
			}
			if(!relativeStructureElement.isProcessed()){
				out = processRelativeStructureElement(structureEleInfo, relativeEleProcessConfigure, errors, processorContext);
				out = processRelativeStructureElementForValue((HAPElementStructureLeafRelativeForValue)out, relativeEleProcessConfigure, errors, processorContext);
			}
			break;
		}
		case HAPConstantShared.CONTEXT_ELEMENTTYPE_NODE:
		{
			Map<String, HAPElementStructure> processedChildren = new LinkedHashMap<String, HAPElementStructure>();
			HAPElementStructureNode nodeStructureElement = (HAPElementStructureNode)defStructureElement;
			for(String childName : nodeStructureElement.getChildren().keySet()) { 	
				processedChildren.put(childName, processRelativeInStructureElement(new HAPInfoElement(nodeStructureElement.getChild(childName), structureEleInfo.getElementPath().appendSegment(childName)), relativeEleProcessConfigure, dependency, errors, processorContext));
			}
			nodeStructureElement.setChildren(processedChildren);
			break;
		}
		}
		return out;
	}
	
	private static HAPElementStructure processRelativeStructureElement(HAPInfoElement structureEleInfo, HAPConfigureProcessorRelative relativeEleProcessConfigure, List<HAPServiceData> errors, HAPContextProcessor processContext){
		HAPElementStructureLeafRelative defStructureElementRelative = (HAPElementStructureLeafRelative)structureEleInfo.getElement();
		HAPElementStructureLeafRelative out = defStructureElementRelative;
		
		HAPReferenceElement pathReference = defStructureElementRelative.getReference();
		HAPResultReferenceResolve resolveInfo = HAPUtilityProcessRelativeElement.resolveElementReference(pathReference, relativeEleProcessConfigure==null?null:relativeEleProcessConfigure.getResolveStructureElementReferenceConfigure(), processContext);
		
		if(resolveInfo==null) {
			errors.add(HAPServiceData.createFailureData(structureEleInfo, HAPConstant.ERROR_PROCESSCONTEXT_NOREFFEREDNODE));
			if(!relativeEleProcessConfigure.isTolerantNoParentForRelative()) {
				throw new RuntimeException();
			}
		}
		else {
			resolveInfo.finalElement = resolveFinalElement(resolveInfo.elementInfoSolid, relativeEleProcessConfigure.isInheritRule());
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
				errors.add(HAPServiceData.createFailureData(structureEleInfo, HAPConstant.ERROR_PROCESSCONTEXT_NOREFFEREDNODE));
				if(!relativeEleProcessConfigure.isTolerantNoParentForRelative()) {
					throw new RuntimeException();
				}
			}
			
			out.setResolvedInfo(new HAPInfoRelativeResolve(resolveInfo.structureId, new HAPComplexPath(resolveInfo.rootName, resolveInfo.elementInfoSolid.solvedPath), resolveInfo.elementInfoSolid.remainPath, resolveInfo.finalElement));
		}
		return out;
	}
	
	private static HAPElementStructure processRelativeStructureElementForValue(HAPElementStructureLeafRelativeForValue defStructureElementRelative, HAPConfigureProcessorRelative relativeEleProcessConfigure, List<HAPServiceData> errors, HAPContextProcessor processContext){
		HAPElementStructure out = defStructureElementRelative;

		HAPElementStructure resolvedSolidElement = defStructureElementRelative.getResolveInfo().getSolidElement();
		HAPElementStructure relativeContextEle = defStructureElementRelative.getDefinition();
		if(relativeContextEle==null) {
			defStructureElementRelative.setDefinition(resolvedSolidElement);
		}
		else {
			//figure out matchers
			List<HAPPathElementMapping> mappingPaths = new ArrayList<HAPPathElementMapping>();
			HAPUtilityStructure.mergeElement(defStructureElementRelative.getResolveInfo().getSolidElement(), relativeContextEle, false, mappingPaths, null, processContext.getRuntimeEnvironment());
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
					HAPVariableDataInfo solidParentDataInfo = ((HAPElementStructureLeafData)solidParent).getDataInfo();
					HAPMatchers ruleMatchers = HAPMatcherUtility.reversMatchers(HAPMatcherUtility.cascadeMatchers(solidParentDataInfo.getRuleMatchers()==null?null:solidParentDataInfo.getRuleMatchers().getReverseMatchers(), noVoidMatchers.get("")));
					for(HAPDataRule rule : solidParentDataInfo.getRules()) {
						HAPVariableDataInfo relativeDataInfo = ((HAPElementStructureLeafData)relativeContextEle).getDataInfo();
						relativeDataInfo.addRule(rule);
						relativeDataInfo.setRuleMatchers(ruleMatchers, solidParentDataInfo.getRuleCriteria());
					}
				}
			}
		}
		return out;
	}
}