package com.nosliw.data.core.domain.entity.valuestructure;

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
import com.nosliw.data.core.data.criteria.HAPDataTypeCriteria;
import com.nosliw.data.core.data.criteria.HAPUtilityCriteria;
import com.nosliw.data.core.data.variable.HAPDataRule;
import com.nosliw.data.core.data.variable.HAPVariableDataInfo;
import com.nosliw.data.core.domain.HAPDomainValueStructure;
import com.nosliw.data.core.matcher.HAPMatcherUtility;
import com.nosliw.data.core.matcher.HAPMatchers;
import com.nosliw.data.core.runtime.HAPRuntimeEnvironment;
import com.nosliw.data.core.structure.HAPElementStructure;
import com.nosliw.data.core.structure.HAPElementStructureLeafData;
import com.nosliw.data.core.structure.HAPElementStructureLeafRelative;
import com.nosliw.data.core.structure.HAPElementStructureLeafRelativeForDefinition;
import com.nosliw.data.core.structure.HAPElementStructureLeafRelativeForValue;
import com.nosliw.data.core.structure.HAPElementStructureNode;
import com.nosliw.data.core.structure.HAPInfoElement;
import com.nosliw.data.core.structure.HAPInfoPathToSolidRoot;
import com.nosliw.data.core.structure.HAPInfoRelativeResolve;
import com.nosliw.data.core.structure.HAPRootStructure;
import com.nosliw.data.core.structure.HAPUtilityStructure;
import com.nosliw.data.core.structure.reference.HAPCandidatesValueContext;
import com.nosliw.data.core.structure.reference.HAPInfoDesendantResolve;
import com.nosliw.data.core.structure.reference.HAPInfoReferenceResolve;
import com.nosliw.data.core.structure.reference.HAPReferenceElementInValueContext;
import com.nosliw.data.core.structure.reference.HAPUtilityStructureElementReference;

public class HAPUtilityProcessRelativeElement {

	//resolve the remain path part
	public static HAPElementStructure resolveFinalElement(HAPInfoDesendantResolve resolveInfo, Boolean relativeInheritRule) {
		HAPElementStructure out = null;
		if(relativeInheritRule==null)   relativeInheritRule = true;
		
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
							if(HAPUtilityBasic.isEquals(rulePath, remainPath))  subPath = "";
							else if(remainPath.contains(rulePath+"."))  subPath = remainPath.substring(rulePath.length()+1);

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
	
	public static void processRelativeInStructure(HAPDefinitionEntityValueStructure valueStructure, HAPCandidatesValueContext parentValueContexts, HAPDomainValueStructure valueStructureDomain, HAPConfigureProcessorRelative processRelativeConfigure, Set<String>  dependency, List<HAPServiceData> errors, HAPRuntimeEnvironment runtimeEnv) {
		if(processRelativeConfigure==null)  processRelativeConfigure = new HAPConfigureProcessorRelative(); 
		
		for(HAPRootStructure rootStructure : valueStructure.getAllRoots()) {
			HAPElementStructure rootElement = processRelativeInStructureElement(new HAPInfoElement(rootStructure.getDefinition(), new HAPComplexPath(rootStructure.getName())), parentValueContexts, valueStructureDomain, processRelativeConfigure, dependency, errors, runtimeEnv);
			rootStructure.setDefinition(rootElement);
		}
	}
	
	private static HAPElementStructure processRelativeInStructureElement(HAPInfoElement structureEleInfo, HAPCandidatesValueContext parentValueContexts, HAPDomainValueStructure valueStructureDomain, HAPConfigureProcessorRelative relativeEleProcessConfigure, Set<String>  dependency, List<HAPServiceData> errors, HAPRuntimeEnvironment runtimeEnv) {
		HAPElementStructure defStructureElement = structureEleInfo.getElement();
		HAPElementStructure out = defStructureElement;
		switch(defStructureElement.getType()) {
		case HAPConstantShared.CONTEXT_ELEMENTTYPE_RELATIVE_FOR_DEFINITION:
		{
			HAPElementStructureLeafRelativeForDefinition relativeStructureElement = (HAPElementStructureLeafRelativeForDefinition)defStructureElement;
			if(dependency!=null)  dependency.add(relativeStructureElement.getReference().getParentValueContextName());
			if(!relativeStructureElement.isProcessed()){
				out = processRelativeStructureElement(structureEleInfo, parentValueContexts, valueStructureDomain, relativeEleProcessConfigure, errors, runtimeEnv);
			}
			break;
		}
		case HAPConstantShared.CONTEXT_ELEMENTTYPE_RELATIVE_FOR_VALUE:
		{
			HAPElementStructureLeafRelativeForValue relativeStructureElement = (HAPElementStructureLeafRelativeForValue)defStructureElement;
			if(dependency!=null)  dependency.add(relativeStructureElement.getReference().getParentValueContextName());
			if(!relativeStructureElement.isProcessed()){
				out = processRelativeStructureElement(structureEleInfo, parentValueContexts, valueStructureDomain, relativeEleProcessConfigure, errors, runtimeEnv);
				out = processRelativeStructureElementForValue((HAPElementStructureLeafRelativeForValue)out, relativeEleProcessConfigure, errors, runtimeEnv);
			}
			break;
		}
		case HAPConstantShared.CONTEXT_ELEMENTTYPE_NODE:
		{
			Map<String, HAPElementStructure> processedChildren = new LinkedHashMap<String, HAPElementStructure>();
			HAPElementStructureNode nodeStructureElement = (HAPElementStructureNode)defStructureElement;
			for(String childName : nodeStructureElement.getChildren().keySet()) { 	
				processedChildren.put(childName, processRelativeInStructureElement(new HAPInfoElement(nodeStructureElement.getChild(childName), structureEleInfo.getElementPath().appendSegment(childName)), parentValueContexts, valueStructureDomain, relativeEleProcessConfigure, dependency, errors, runtimeEnv));
			}
			nodeStructureElement.setChildren(processedChildren);
			break;
		}
		}
		return out;
	}
	
	private static HAPElementStructure processRelativeStructureElement(HAPInfoElement structureEleInfo, HAPCandidatesValueContext parentValueContexts, HAPDomainValueStructure valueStructureDomain, HAPConfigureProcessorRelative relativeEleProcessConfigure, List<HAPServiceData> errors, HAPRuntimeEnvironment runtimeEnv){
		HAPElementStructureLeafRelative defStructureElementRelative = (HAPElementStructureLeafRelative)structureEleInfo.getElement();
		HAPElementStructureLeafRelative out = defStructureElementRelative;
		
		HAPReferenceElementInValueContext pathReference = defStructureElementRelative.getReference();
		HAPInfoReferenceResolve resolveInfo = HAPUtilityStructureElementReference.resolveElementReference(pathReference, parentValueContexts, relativeEleProcessConfigure==null?null:relativeEleProcessConfigure.getResolveStructureElementReferenceConfigure(), valueStructureDomain);
		
		if(resolveInfo==null) {
			errors.add(HAPServiceData.createFailureData(structureEleInfo, HAPConstant.ERROR_PROCESSCONTEXT_NOREFFEREDNODE));
			if(!relativeEleProcessConfigure.isTolerantNoParentForRelative())  throw new RuntimeException();
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
				if(!relativeEleProcessConfigure.isTolerantNoParentForRelative())  throw new RuntimeException();
			}
			
			out.setResolvedInfo(new HAPInfoRelativeResolve(resolveInfo.structureId, new HAPComplexPath(resolveInfo.rootName, resolveInfo.elementInfoSolid.solvedPath), resolveInfo.elementInfoSolid.remainPath, resolveInfo.finalElement));
		}
		return out;
	}
	
	private static HAPElementStructure processRelativeStructureElementForValue(HAPElementStructureLeafRelativeForValue defStructureElementRelative, HAPConfigureProcessorRelative relativeEleProcessConfigure, List<HAPServiceData> errors, HAPRuntimeEnvironment runtimeEnv){
		HAPElementStructure out = defStructureElementRelative;

		HAPElementStructure resolvedSolidElement = defStructureElementRelative.getResolveInfo().getSolidElement();
		HAPElementStructure relativeContextEle = defStructureElementRelative.getDefinition();
		if(relativeContextEle==null) {
			defStructureElementRelative.setDefinition(resolvedSolidElement);
		}
		else {
			//figure out matchers
			Map<String, HAPMatchers> matchers = new LinkedHashMap<String, HAPMatchers>();
			HAPUtilityStructure.mergeElement(defStructureElementRelative.getResolveInfo().getSolidElement(), relativeContextEle, false, matchers, null, runtimeEnv);
			//remove all the void matchers
			Map<String, HAPMatchers> noVoidMatchers = new LinkedHashMap<String, HAPMatchers>();
			for(String p : matchers.keySet()){
				HAPMatchers match = matchers.get(p);
				if(!match.isVoid()){
					noVoidMatchers.put(p, match);
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
