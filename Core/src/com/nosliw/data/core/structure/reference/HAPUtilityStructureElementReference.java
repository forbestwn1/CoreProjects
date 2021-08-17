package com.nosliw.data.core.structure.reference;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import com.nosliw.common.path.HAPComplexPath;
import com.nosliw.common.path.HAPPath;
import com.nosliw.common.utils.HAPBasicUtility;
import com.nosliw.common.utils.HAPConstant;
import com.nosliw.common.utils.HAPConstantShared;
import com.nosliw.data.core.data.criteria.HAPCriteriaUtility;
import com.nosliw.data.core.data.criteria.HAPDataTypeCriteria;
import com.nosliw.data.core.data.variable.HAPDataRule;
import com.nosliw.data.core.data.variable.HAPVariableDataInfo;
import com.nosliw.data.core.structure.HAPElementStructure;
import com.nosliw.data.core.structure.HAPElementStructureLeafData;
import com.nosliw.data.core.structure.HAPReferenceElement;
import com.nosliw.data.core.structure.HAPRootStructure;
import com.nosliw.data.core.structure.HAPStructure;
import com.nosliw.data.core.structure.HAPUtilityStructure;
import com.nosliw.data.core.structure.HAPUtilityStructureReference;
import com.nosliw.data.core.valuestructure.HAPContainerStructure;

public class HAPUtilityStructureElementReference {

	public static HAPInfoReferenceResolve resolveElementReference(String elementReferenceLiterate, HAPStructure parentStructure, String mode, Boolean relativeInheritRule, Set<String> elementTypes){
		HAPInfoReferenceResolve resolveInfo = analyzeElementReference(elementReferenceLiterate, parentStructure, mode, elementTypes);
		if(resolveInfo!=null)  resolveInfo.resolvedElement = resolveElement(resolveInfo.realSolidSolved, relativeInheritRule);
		return resolveInfo;
	}
	
	public static HAPInfoReferenceResolve analyzeElementReference(String elementReferenceLiterate, HAPStructure parentStructure, String mode, Set<String> elementTypes){
		HAPReferenceElement elementReference = new HAPReferenceElement(elementReferenceLiterate); 
		return analyzeElementReference(elementReference, parentStructure, mode, elementTypes);
	}
	
	//find best resolved element from structure 
	public static HAPInfoReferenceResolve analyzeElementReference(HAPReferenceElement elementReference, HAPStructure parentStructure, String mode, Set<String> elementTypes){
		if(parentStructure==null)   return null;
		
		//normalize element reference
		elementReference = HAPUtilityStructureReference.normalizeElementReference(elementReference, parentStructure.getStructureType());
		
		//find candidate from structure by root reference
		List<HAPRootStructure> candidatesRoot = parentStructure.resolveRoot(elementReference.getRootReference(), false);
		
		//resolve path, 
		//find all candidate
		List<HAPInfoReferenceResolve> resolveCandidates = new ArrayList<HAPInfoReferenceResolve>();
		for(HAPRootStructure root : candidatesRoot) {
			HAPInfoReferenceResolve resolved = new HAPInfoReferenceResolve(); 
			String path = elementReference.getPath();
			resolved.referredRoot = root;
			resolved.path = new HAPComplexPath(root.getLocalId(), path);
			resolved.rootReference = parentStructure.getRootReferenceById(root.getLocalId());

			resolved.realSolidSolved = HAPUtilityStructure.resolveDescendant(root.getDefinition().getSolidStructureElement(), path);
			resolved.realSolved = HAPUtilityStructure.resolveDescendant(root.getDefinition(), path);
			
			if(resolved!=null) {
				if(elementTypes==null || elementTypes.contains(resolved.realSolidSolved.resolvedElement.getType())) {
					resolveCandidates.add(resolved);
					if(HAPConstant.RESOLVEPARENTMODE_FIRST.equals(mode))   break;
				}
			}
		}
		
		//find best resolve from candidate
		//remaining path is shortest
		HAPInfoReferenceResolve out = null;
		int length = 99999;
		for(HAPInfoReferenceResolve candidate : resolveCandidates) {
			HAPPath remainingPath = candidate.realSolidSolved.remainPath;
			if(remainingPath.isEmpty()) {
				out = candidate;
				break;
			}
			else {
				if(remainingPath.getLength()<length) {
					length = remainingPath.getLength();
					out = candidate;
				}
			}
		}
		return out;
	}
	
	//resolve the remain path part
	public static HAPElementStructure resolveElement(HAPInfoDesendantResolve resolveInfo, Boolean relativeInheritRule) {
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
				HAPDataTypeCriteria childCriteria = HAPCriteriaUtility.getChildCriteriaByPath(dataLeafEle.getCriteria(), resolveInfo.remainPath.getPath());
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
	
	//find exact physical node
	public static boolean isPhysicallySolved(HAPInfoReferenceResolve solve) {
		return solve!=null && (solve.resolvedElement!=null && solve.realSolidSolved.remainPath.isEmpty());
	}

	//find node
	public static boolean isLogicallySolved(HAPInfoReferenceResolve solve) {
		return solve!=null && solve.resolvedElement!=null;
	}

	public static HAPStructure getReferedStructure(String name, HAPContainerStructure parents, HAPStructure self) {
		if(HAPConstantShared.DATAASSOCIATION_RELATEDENTITY_SELF.equals(name))  return self;
		else return parents.getStructure(name);
	}


}
