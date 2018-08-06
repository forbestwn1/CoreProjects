package com.nosliw.data.context;

import com.nosliw.data.core.HAPDataTypeHelper;
import com.nosliw.data.core.expressionsuite.HAPExpressionSuiteManager;
import com.nosliw.data.core.runtime.HAPRuntime;

public class HAPEnvContextProcessor {

	public HAPEnvContextProcessor(HAPDataTypeHelper dataTypeHelper, HAPRuntime runtime, HAPExpressionSuiteManager expressionManager) {
		this.dataTypeHelper = dataTypeHelper;
		this.runtime = runtime;
		this.expressionManager = expressionManager;
	}
	
	public HAPDataTypeHelper dataTypeHelper;
	public HAPRuntime runtime;
	public HAPExpressionSuiteManager expressionManager;
	
}
