package com.nosliw.uiresource.common;

import java.util.HashSet;
import java.util.Set;

import com.nosliw.common.utils.HAPConstantShared;
import com.nosliw.core.application.common.structure.HAPRequirementContextProcessor;
import com.nosliw.data.core.data.HAPDataTypeHelper;
import com.nosliw.data.core.domain.entity.expression.data.HAPManagerExpression;
import com.nosliw.data.core.resource.HAPManagerResourceDefinition;
import com.nosliw.data.core.runtime.HAPRuntime;
import com.nosliw.data.core.service.definition.HAPManagerServiceDefinition;

public class HAPUtilityCommon {

	public static HAPRequirementContextProcessor getDefaultContextProcessorRequirement(
			HAPManagerResourceDefinition resourceDefMan,
			HAPDataTypeHelper dataTypeHelper, 
			HAPRuntime runtime, 
			HAPManagerExpression expressionMan,
			HAPManagerServiceDefinition serviceDefinitionManager) { 
		Set<String> inheritanceExcludedInfo = new HashSet<String>();
		inheritanceExcludedInfo.add(HAPConstantShared.UIRESOURCE_CONTEXTINFO_INSTANTIATE);
		inheritanceExcludedInfo.add(HAPConstantShared.UIRESOURCE_CONTEXTINFO_RELATIVECONNECTION);
		HAPRequirementContextProcessor contextProcessRequirement = new HAPRequirementContextProcessor(resourceDefMan, dataTypeHelper, runtime, expressionMan, serviceDefinitionManager, inheritanceExcludedInfo);
		return contextProcessRequirement;
	}
	
	public static Set<String> getDefaultInheritanceExcludedInfo(){
		Set<String> inheritanceExcludedInfo = new HashSet<String>();
		inheritanceExcludedInfo.add(HAPConstantShared.UIRESOURCE_CONTEXTINFO_INSTANTIATE);
		inheritanceExcludedInfo.add(HAPConstantShared.UIRESOURCE_CONTEXTINFO_RELATIVECONNECTION);
		return  inheritanceExcludedInfo;
	}
	
}
