package com.nosliw.data.core.script.context;

import java.util.Set;

import com.nosliw.data.core.HAPDataTypeHelper;
import com.nosliw.data.core.expressionsuite.HAPExpressionSuiteManager;
import com.nosliw.data.core.runtime.HAPRuntime;
import com.nosliw.data.core.service.provide.HAPManagerServiceDefinition;

public class HAPRequirementContextProcessor {

	public HAPRequirementContextProcessor(HAPDataTypeHelper dataTypeHelper, HAPRuntime runtime, HAPExpressionSuiteManager expressionManager, HAPManagerServiceDefinition serviceDefinitionManager, Set<String> inheritanceExcludedInfo) {
		this.dataTypeHelper = dataTypeHelper;
		this.runtime = runtime;
		this.expressionManager = expressionManager;
		this.serviceDefinitionManager = serviceDefinitionManager; 
		this.inheritanceExcludedInfo = inheritanceExcludedInfo;
	}
	
	public HAPDataTypeHelper dataTypeHelper;
	public HAPRuntime runtime;
	public HAPExpressionSuiteManager expressionManager;
	public HAPManagerServiceDefinition serviceDefinitionManager;
	public Set<String> inheritanceExcludedInfo;
	
}
