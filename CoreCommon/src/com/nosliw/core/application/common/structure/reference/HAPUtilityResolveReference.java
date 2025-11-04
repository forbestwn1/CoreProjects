package com.nosliw.core.application.common.structure.reference;

import java.util.Set;

import com.nosliw.common.path.HAPComplexPath;
import com.nosliw.common.path.HAPPath;
import com.nosliw.common.utils.HAPConstantShared;
import com.nosliw.common.utils.HAPUtilityBasic;
import com.nosliw.core.application.common.datadefinition.HAPDataDefinitionWritable;
import com.nosliw.core.application.common.datadefinition.HAPDefinitionDataRule;
import com.nosliw.core.application.common.structure.HAPElementStructure;
import com.nosliw.core.application.common.structure.HAPElementStructureLeafData;
import com.nosliw.core.application.common.structure.HAPRootInStructure;
import com.nosliw.core.application.common.structure.HAPStructure;
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

			resolved.elementInfoSolid = new HAPResultDesendantResolve(root.getDefinition().getSolidStructureElement(), new HAPPath(), new HAPPath(path)); 
			if(resolved.elementInfoSolid!=null) {
				resolved.elementInfoOriginal = new HAPResultDesendantResolve(root.getDefinition(), new HAPPath(), new HAPPath(path));
				
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
					out = new HAPElementStructureLeafData(new HAPDataDefinitionWritable(childCriteria)); 
					
					//inherit rule from parent
					if(relativeInheritRule) {
						HAPDataDefinitionWritable solidParentDataInfo = ((HAPElementStructureLeafData)candidateNode).getDataDefinition();
						
						for(HAPDefinitionDataRule ruleDef : solidParentDataInfo.getRules()) {
							String subPath = null;
							String rulePath = ruleDef.getPath();
							String remainPath = resolveInfo.remainPath.getPath();
							if(HAPUtilityBasic.isEquals(rulePath, remainPath)) {
								subPath = "";
							} else if(remainPath.contains(rulePath+".")) {
								subPath = remainPath.substring(rulePath.length()+1);
							}

							if(subPath!=null) {
								HAPDefinitionDataRule newRuleDef = ruleDef.cloneDataRuleDef();
								newRuleDef.setPath(subPath);
								((HAPElementStructureLeafData)out).getDataDefinition().addRule(newRuleDef);
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
