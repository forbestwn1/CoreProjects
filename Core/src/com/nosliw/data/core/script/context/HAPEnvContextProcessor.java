package com.nosliw.data.core.script.context;

import java.util.Set;

import com.nosliw.data.core.HAPDataTypeHelper;
import com.nosliw.data.core.expressionsuite.HAPExpressionSuiteManager;
import com.nosliw.data.core.runtime.HAPRuntime;

public class HAPEnvContextProcessor {

	public HAPEnvContextProcessor(HAPDataTypeHelper dataTypeHelper, HAPRuntime runtime, HAPExpressionSuiteManager expressionManager, Set<String> inheritanceExcludedInfo) {
		this.dataTypeHelper = dataTypeHelper;
		this.runtime = runtime;
		this.expressionManager = expressionManager;
		this.inheritanceExcludedInfo = inheritanceExcludedInfo;
	}
	
	public HAPDataTypeHelper dataTypeHelper;
	public HAPRuntime runtime;
	public HAPExpressionSuiteManager expressionManager;
	public Set<String> inheritanceExcludedInfo;
	
}
