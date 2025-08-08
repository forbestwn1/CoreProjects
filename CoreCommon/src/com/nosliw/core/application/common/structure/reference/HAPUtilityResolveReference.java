package com.nosliw.core.application.common.structure.reference;

import java.util.Set;

import com.nosliw.common.path.HAPComplexPath;
import com.nosliw.common.utils.HAPConstantShared;
import com.nosliw.common.utils.HAPUtilityBasic;
import com.nosliw.core.application.common.structure.HAPElementStructure;
import com.nosliw.core.application.common.structure.HAPElementStructureLeafData;
import com.nosliw.core.application.common.structure.HAPRootInStructure;
import com.nosliw.core.application.common.structure.HAPStructure;
import com.nosliw.core.application.common.structure.HAPUtilityElement;
import com.nosliw.core.application.common.variable.HAPDataRule;
import com.nosliw.core.application.common.variable.HAPVariableDataInfo;
import com.nosliw.core.application.valueport.HAPResultDesendantResolve;
import com.nosliw.core.application.valueport.HAPResultReferenceResolve;
import com.nosliw.core.data.criteria.HAPDataTypeCriteria;
import com.nosliw.core.data.criteria.HAPUtilityCriteria;

public class HAPUtilityResolveReference {

	public static HAPResultReferenceResolve analyzeElementReference(String elementPath, HAPStructure valueStructure, HAPConfigureResolveElementReference resolveConfigure) {
		HAPComplexPath complexPath = new HAPComplexPath(elementPath);
		String rootName = complexPath.getRoot();
		String path = complexPath.getPathStr();
		
		HAPRootInStructure root = valueStructure.getRootByName(rootName);
		if(root!=null) {
			HAPResultReferenceResolve resolved = new HAPResultReferenceResolve(); 
			resolved.rootName = rootName;
			resolved.elementPath = path;
			resolved.fullPath = elementPath;

			resolved.elementInfoSolid = HAPUtilityElement.resolveDescendant(root.getDefinition().getSolidStructureElement(), path);
			if(resolved.elementInfoSolid!=null) {
				resolved.elementInfoOriginal = HAPUtilityElement.resolveDescendant(root.getDefinition(), path);
				
				Set<String> elementTypes = resolveConfigure.candidateElementTypes;
				if(elementTypes==null || elementTypes.contains(resolved.elementInfoSolid.resolvedElement.getType())) {
					return resolved;
				}
			}
		}
		return null;
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
}
