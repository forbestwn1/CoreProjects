package com.nosliw.data.core.structure;

import java.util.Set;

import com.nosliw.data.core.valuestructure.HAPContainerVariableCriteriaInfo;

public class HAPUtilityStructureDataAssociation {

	public static HAPStructure enhanceVariableMapping(HAPStructure mapping, HAPContainerVariableCriteriaInfo parentVarsContainer) {
		
		Set<String> parentVarIdPaths = parentVarsContainer.getVariableCriteriaInfos().keySet();
		for(String parentVarIdPath : parentVarIdPaths) {
			Set<String> parentVarNamePaths = parentVarsContainer.getVariableAlias(parentVarIdPath);
			
			
			
		}
		
		return mapping;
	}
	
	
}
