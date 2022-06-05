package com.nosliw.data.core.domain.entity.valuestructure;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.nosliw.common.exception.HAPServiceData;
import com.nosliw.common.path.HAPComplexPath;
import com.nosliw.common.path.HAPPath;
import com.nosliw.common.utils.HAPBasicUtility;
import com.nosliw.common.utils.HAPConstant;
import com.nosliw.common.utils.HAPConstantShared;
import com.nosliw.common.utils.HAPUtilityNamingConversion;
import com.nosliw.data.core.data.criteria.HAPDataTypeCriteria;
import com.nosliw.data.core.data.criteria.HAPUtilityCriteria;
import com.nosliw.data.core.data.variable.HAPDataRule;
import com.nosliw.data.core.data.variable.HAPVariableDataInfo;
import com.nosliw.data.core.domain.HAPDomainValueStructure;
import com.nosliw.data.core.matcher.HAPMatcherUtility;
import com.nosliw.data.core.matcher.HAPMatchers;
import com.nosliw.data.core.runtime.HAPRuntimeEnvironment;
import com.nosliw.data.core.structure.HAPElementStructure;
import com.nosliw.data.core.structure.HAPElementStructureLeafConstant;
import com.nosliw.data.core.structure.HAPElementStructureLeafData;
import com.nosliw.data.core.structure.HAPElementStructureLeafRelative;
import com.nosliw.data.core.structure.HAPElementStructureNode;
import com.nosliw.data.core.structure.HAPInfoElement;
import com.nosliw.data.core.structure.HAPInfoPathToSolidRoot;
import com.nosliw.data.core.structure.HAPRootStructure;
import com.nosliw.data.core.structure.HAPUtilityStructure;
import com.nosliw.data.core.structure.reference.HAPCandidatesValueStructureComplex;
import com.nosliw.data.core.structure.reference.HAPInfoDesendantResolve;
import com.nosliw.data.core.structure.reference.HAPInfoReferenceResolve;
import com.nosliw.data.core.structure.reference.HAPReferenceElementInStructureComplex;
import com.nosliw.data.core.structure.reference.HAPUtilityStructureElementReference;

public class HAPUtilityProcessRelativeElement {

	//resolve the remain path part
	public static HAPElementStructure resolveFinalElement(HAPInfoDesendantResolve resolveInfo, Boolean relativeInheritRule) {
		HAPElementStructure out = null;
		if(relativeInheritRule==null)   relativeInheritRule = true;
		
		if(resolveInfo.remainPath.isEmpty()) {
			//exactly match with path
			out = resolveInfo.resolvedElement;
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
							if(HAPBasicUtility.isEquals(rulePath, remainPath))  subPath = "";
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
	
	public static void processRelativeInStructure(HAPDefinitionEntityValueStructure valueStructure, HAPCandidatesValueStructureComplex parentValueStructureComplex, HAPDomainValueStructure valueStructureDomain, HAPConfigureProcessorRelative processRelativeConfigure, Set<String>  dependency, List<HAPServiceData> errors, HAPRuntimeEnvironment runtimeEnv) {
		for(HAPRootStructure rootStructure : valueStructure.getAllRoots()) {
			HAPElementStructure rootElement = processRelativeInStructureElement(new HAPInfoElement(rootStructure.getDefinition(), new HAPComplexPath(rootStructure.getName())), parentValueStructureComplex, valueStructureDomain, processRelativeConfigure, dependency, errors, runtimeEnv);
			rootStructure.setDefinition(rootElement);
		}
	}
	
	private static HAPElementStructure processRelativeInStructureElement(HAPInfoElement structureEleInfo, HAPCandidatesValueStructureComplex parentValueStructureComplex, HAPDomainValueStructure valueStructureDomain, HAPConfigureProcessorRelative relativeEleProcessConfigure, Set<String>  dependency, List<HAPServiceData> errors, HAPRuntimeEnvironment runtimeEnv) {
		HAPElementStructure defStructureElement = structureEleInfo.getElement();
		HAPElementStructure out = defStructureElement;
		switch(defStructureElement.getType()) {
		case HAPConstantShared.CONTEXT_ELEMENTTYPE_RELATIVE:
			HAPElementStructureLeafRelative relativeStructureElement = (HAPElementStructureLeafRelative)defStructureElement;
			if(dependency!=null)  dependency.add(relativeStructureElement.getPath().getParentComplexName());
			if(!relativeStructureElement.isProcessed()){
				out = processRelativeContextDefinitionElement(structureEleInfo, parentValueStructureComplex, valueStructureDomain, relativeEleProcessConfigure, errors, runtimeEnv);
			}
			break;
		case HAPConstantShared.CONTEXT_ELEMENTTYPE_NODE:
			Map<String, HAPElementStructure> processedChildren = new LinkedHashMap<String, HAPElementStructure>();
			HAPElementStructureNode nodeStructureElement = (HAPElementStructureNode)defStructureElement;
			for(String childName : nodeStructureElement.getChildren().keySet()) { 	
				processedChildren.put(childName, processRelativeInStructureElement(new HAPInfoElement(nodeStructureElement.getChild(childName), structureEleInfo.getElementPath().appendSegment(childName)), parentValueStructureComplex, valueStructureDomain, relativeEleProcessConfigure, dependency, errors, runtimeEnv));
			}
			nodeStructureElement.setChildren(processedChildren);
			break;
		}
		return out;
	}
	
	private static HAPElementStructure processRelativeContextDefinitionElement(HAPInfoElement structureEleInfo, HAPCandidatesValueStructureComplex parentValueStructureComplex, HAPDomainValueStructure valueStructureDomain, HAPConfigureProcessorRelative relativeEleProcessConfigure, List<HAPServiceData> errors, HAPRuntimeEnvironment runtimeEnv){
		HAPElementStructureLeafRelative defStructureElementRelative = (HAPElementStructureLeafRelative)structureEleInfo.getElement();
		HAPElementStructure out = defStructureElementRelative;
		
		HAPReferenceElementInStructureComplex pathReference = defStructureElementRelative.getPath();
		HAPInfoReferenceResolve resolveInfo = HAPUtilityStructureElementReference.resolveElementReference(pathReference, parentValueStructureComplex, relativeEleProcessConfigure.getResolveStructureElementReferenceConfigure(), valueStructureDomain);
		resolveInfo.finalElement = resolveFinalElement(resolveInfo.elementInfoSolid, relativeEleProcessConfigure.isInheritRule());
		
		if(resolveInfo==null || resolveInfo.referredRoot==null) {
			errors.add(HAPServiceData.createFailureData(structureEleInfo, HAPConstant.ERROR_PROCESSCONTEXT_NOREFFEREDNODE));
			if(!relativeEleProcessConfigure.isTolerantNoParentForRelative())  throw new RuntimeException();
		}
		else {
			switch(resolveInfo.referredRoot.getDefinition().getType()) {
			case HAPConstantShared.CONTEXT_ELEMENTTYPE_CONSTANT:
			{
				//if refer to a constant element
				out = new HAPElementStructureLeafConstant();
				Object constantValue = ((HAPElementStructureLeafConstant)resolveInfo.referredRoot.getDefinition()).getValue();
				((HAPElementStructureLeafConstant)out).setValue(constantValue);
				break;
			}
			default:
			{
				HAPElementStructure solvedContextEle = resolveInfo.finalElement; 
				if(solvedContextEle!=null){
					//refer to solid
					if(relativeEleProcessConfigure.isTrackingToSolid()) {
						String refRootId = null;
						String refPath = null;
						HAPElementStructure parentContextEle = resolveInfo.elementInfoOriginal.resolvedElement; 
						if(parentContextEle.getType().equals(HAPConstantShared.CONTEXT_ELEMENTTYPE_RELATIVE)) {
							HAPInfoPathToSolidRoot parentSolidNodeRef = ((HAPElementStructureLeafRelative)parentContextEle).getSolidNodeReference();
							refRootId = parentSolidNodeRef.getRootNodeId();
							refPath = HAPUtilityNamingConversion.cascadePath(parentSolidNodeRef.getPath().getPath(), resolveInfo.elementInfoSolid.remainPath.getPath());
						}
						else {
							refRootId = resolveInfo.referredRoot.getId();
							refPath = HAPUtilityNamingConversion.cascadePath(resolveInfo.elementInfoSolid.solvedPath.getPath(), resolveInfo.elementInfoSolid.remainPath.getPath());
						}
						defStructureElementRelative.setSolidNodeReference(new HAPInfoPathToSolidRoot(refRootId, new HAPPath(refPath)));
					}
					
					HAPElementStructure relativeContextEle = defStructureElementRelative.getDefinition();
					if(relativeContextEle==null) {
						defStructureElementRelative.setDefinition(solvedContextEle.cloneStructureElement());
					}
					else {
						//figure out matchers
						Map<String, HAPMatchers> matchers = new LinkedHashMap<String, HAPMatchers>();
						HAPUtilityStructure.mergeElement(solvedContextEle, relativeContextEle, false, matchers, null, runtimeEnv);
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
							HAPElementStructure solidParent = solvedContextEle.getSolidStructureElement();
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
				}
				else{
					//not find parent node, throw exception
					errors.add(HAPServiceData.createFailureData(structureEleInfo, HAPConstant.ERROR_PROCESSCONTEXT_NOREFFEREDNODE));
					if(!relativeEleProcessConfigure.isTolerantNoParentForRelative())  throw new RuntimeException();
				}
			}
			}			
		}

		return out;
	}

}
