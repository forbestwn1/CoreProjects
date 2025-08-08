package com.nosliw.core.xxx.application.valueport;

import java.util.Map;

import com.nosliw.common.utils.HAPConstantShared;
import com.nosliw.common.utils.HAPUtilityBasic;
import com.nosliw.core.application.HAPDomainValueStructure;
import com.nosliw.core.application.common.structure.HAPElementStructure;
import com.nosliw.core.application.common.structure.HAPElementStructureLeafData;
import com.nosliw.core.application.common.withvariable.HAPContainerVariableInfo;
import com.nosliw.core.application.valueport.HAPIdElement;
import com.nosliw.core.data.criteria.HAPInfoCriteria;

public class HAPUtilityValuePortVariable {

	public static void updateValuePortElements(HAPContainerVariableInfo varInfoContainer, HAPDomainValueStructure valueStructureDomain) {
		Map<String, HAPIdElement> variables = varInfoContainer.getVariables();
		for(String key : variables.keySet()) {
			HAPIdElement varId = variables.get(key);
			HAPInfoCriteria varCriteriaInfo = varInfoContainer.getVaraibleCriteriaInfo(key);
			HAPElementStructure structureEle = HAPUtilityValuePort.getInternalElement(varId, valueStructureDomain);
			String eleType = structureEle.getType();
			if(eleType.equals(HAPConstantShared.CONTEXT_ELEMENTTYPE_DATA)) {
				HAPElementStructureLeafData dataEle = (HAPElementStructureLeafData)structureEle;
//				if(dataEle.getStatus().equals(HAPConstantShared.EXPRESSION_VARIABLE_STATUS_OPEN)) 
				{
					if(!HAPUtilityBasic.isEquals(dataEle.getCriteria(), varCriteriaInfo.getCriteria())){
						dataEle.setCriteria(varCriteriaInfo.getCriteria());
//						valueStructureDomain.setIsDirty(true);
					}
				}
			}
		}
	}
	
	public static void buildVariableInfo(HAPContainerVariableInfo varInfoContainer, HAPDomainValueStructure valueStructureDomain) {
		Map<String, HAPIdElement> variables = varInfoContainer.getVariables();
		for(String key : variables.keySet()) {
			HAPInfoCriteria varCriteriaInfo = varInfoContainer.getVaraibleCriteriaInfo(key);
			HAPElementStructure structureEle = HAPUtilityValuePort.getInternalElement(variables.get(key), valueStructureDomain); 
			String eleType = structureEle.getType();
			if(eleType.equals(HAPConstantShared.CONTEXT_ELEMENTTYPE_DATA)) {
				HAPElementStructureLeafData dataEle = (HAPElementStructureLeafData)structureEle;
				varCriteriaInfo.setCriteria(dataEle.getCriteria());
				varCriteriaInfo.setStatus(dataEle.getStatus());
			}
		}
	}
	
}
