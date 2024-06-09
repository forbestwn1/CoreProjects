package com.nosliw.core.application.common.structure;

import java.util.Set;

import com.nosliw.core.application.service.HAPManagerServiceDefinition;
import com.nosliw.data.core.data.HAPDataTypeHelper;
import com.nosliw.data.core.domain.entity.expression.data1.HAPManagerExpression;
import com.nosliw.data.core.resource.HAPManagerResourceDefinition;
import com.nosliw.data.core.runtime.HAPRuntime;

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
