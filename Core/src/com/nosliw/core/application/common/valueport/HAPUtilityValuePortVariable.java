package com.nosliw.core.application.common.valueport;

import java.util.Map;

import com.nosliw.common.utils.HAPConstantShared;
import com.nosliw.common.utils.HAPUtilityBasic;
import com.nosliw.core.application.common.structure.HAPElementStructure;
import com.nosliw.core.application.common.structure.HAPElementStructureLeafData;
import com.nosliw.data.core.data.criteria.HAPInfoCriteria;

public class HAPUtilityValuePortVariable {

	public static void updateValuePortElements(HAPContainerVariableInfo varInfoContainer, HAPWithInternalValuePort withInternalValuePort) {
		Map<String, HAPIdElement> variables = varInfoContainer.getVariables();
		for(String key : variables.keySet()) {
			HAPIdElement varId = variables.get(key);
			HAPInfoCriteria varCriteriaInfo = varInfoContainer.getVaraibleCriteriaInfo(key);
			HAPElementStructure structureEle = HAPUtilityValuePort.getInternalElement(varId, withInternalValuePort);
			String eleType = structureEle.getType();
			if(eleType.equals(HAPConstantShared.CONTEXT_ELEMENTTYPE_DATA)) {
				HAPElementStructureLeafData dataEle = (HAPElementStructureLeafData)structureEle;
//				if(dataEle.getStatus().equals(HAPConstantShared.EXPRESSION_VARIABLE_STATUS_OPEN)) 
				{
					if(!HAPUtilityBasic.isEquals(dataEle.getCriteria(), varCriteriaInfo.getCriteria())){
						HAPValuePort valuePort = HAPUtilityValuePort.getInternalValuePort(varId, withInternalValuePort);
						dataEle.setCriteria(varCriteriaInfo.getCriteria());
						valuePort.updateElement(varId, dataEle);
//						valueStructureDomain.setIsDirty(true);
					}
				}
			}
		}
	}
	
	public static void buildVariableInfo(HAPContainerVariableInfo varInfoContainer, HAPWithInternalValuePort withInternalValuePort) {
		Map<String, HAPIdElement> variables = varInfoContainer.getVariables();
		for(String key : variables.keySet()) {
			HAPInfoCriteria varCriteriaInfo = varInfoContainer.getVaraibleCriteriaInfo(key);
			HAPElementStructure structureEle = HAPUtilityValuePort.getInternalElement(variables.get(key), withInternalValuePort); 
			String eleType = structureEle.getType();
			if(eleType.equals(HAPConstantShared.CONTEXT_ELEMENTTYPE_DATA)) {
				HAPElementStructureLeafData dataEle = (HAPElementStructureLeafData)structureEle;
				varCriteriaInfo.setCriteria(dataEle.getCriteria());
				varCriteriaInfo.setStatus(dataEle.getStatus());
			}
		}
	}
	
}