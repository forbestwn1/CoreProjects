package com.nosliw.uiresource.common;

import java.util.HashSet;
import java.util.Set;

import com.nosliw.common.utils.HAPConstant;
import com.nosliw.data.core.HAPDataTypeHelper;
import com.nosliw.data.core.expressionsuite.HAPExpressionSuiteManager;
import com.nosliw.data.core.runtime.HAPRuntime;
import com.nosliw.data.core.script.context.HAPRequirementContextProcessor;
import com.nosliw.data.core.service.provide.HAPManagerServiceDefinition;

public class HAPUtilityCommon {

	public static HAPRequirementContextProcessor getDefaultContextProcessorRequirement(
			HAPDataTypeHelper dataTypeHelper, 
			HAPRuntime runtime, 
			HAPExpressionSuiteManager expressionMan,
			HAPManagerServiceDefinition serviceDefinitionManager) { 
		Set<String> inheritanceExcludedInfo = new HashSet<String>();
		inheritanceExcludedInfo.add(HAPConstant.UIRESOURCE_CONTEXTINFO_INSTANTIATE);
		inheritanceExcludedInfo.add(HAPConstant.UIRESOURCE_CONTEXTINFO_RELATIVECONNECTION);
		HAPRequirementContextProcessor contextProcessRequirement = new HAPRequirementContextProcessor(dataTypeHelper, runtime, expressionMan, serviceDefinitionManager, inheritanceExcludedInfo);
		return contextProcessRequirement;
	}
}
