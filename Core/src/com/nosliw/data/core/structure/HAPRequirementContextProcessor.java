package com.nosliw.data.core.structure;

import java.util.Set;

import com.nosliw.data.core.data.HAPDataTypeHelper;
import com.nosliw.data.core.expression.HAPManagerExpression;
import com.nosliw.data.core.resource.HAPManagerResourceDefinition;
import com.nosliw.data.core.runtime.HAPRuntime;
import com.nosliw.data.core.service.definition.HAPManagerServiceDefinition;

public class HAPRequirementContextProcessor {

	public HAPRequirementContextProcessor(
			HAPManagerResourceDefinition resourceDefMan,
			HAPDataTypeHelper dataTypeHelper, 
			HAPRuntime runtime, 
			HAPManagerExpression expressionManager, 
			HAPManagerServiceDefinition serviceDefinitionManager, 
			Set<String> inheritanceExcludedInfo) {
		this.resourceDefMan = resourceDefMan;
		this.dataTypeHelper = dataTypeHelper;
		this.runtime = runtime;
		this.expressionManager = expressionManager;
		this.serviceDefinitionManager = serviceDefinitionManager; 
		this.inheritanceExcludedInfo = inheritanceExcludedInfo;
	}
	
	public HAPManagerResourceDefinition resourceDefMan;
	public HAPDataTypeHelper dataTypeHelper;
	public HAPRuntime runtime;
	public HAPManagerExpression expressionManager;
	public HAPManagerServiceDefinition serviceDefinitionManager;
	public Set<String> inheritanceExcludedInfo;
	
}
