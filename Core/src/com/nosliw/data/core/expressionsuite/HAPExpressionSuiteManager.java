package com.nosliw.data.core.expressionsuite;

import java.util.LinkedHashMap;
import java.util.Map;

import com.nosliw.common.utils.HAPProcessTracker;
import com.nosliw.data.core.HAPData;
import com.nosliw.data.core.criteria.HAPDataTypeCriteria;
import com.nosliw.data.core.criteria.HAPVariableInfo;
import com.nosliw.data.core.expression.HAPDefinitionExpression;
import com.nosliw.data.core.expression.HAPExecutableExpression;

public class HAPExpressionSuiteManager {

	public HAPExpressionSuiteManager() {
	}
	
	public HAPExecutableExpression compileExpression(
			HAPDefinitionExpression expression, 
			Map<String, HAPDefinitionExpression> contextExpressionDefinitions, 
			Map<String, HAPVariableInfo> parentVariablesInfo, 
			Map<String, HAPData> contextConstants,
			HAPDataTypeCriteria expectOutput, 
			Map<String, String> configure,
			HAPProcessTracker processTracker) {
		
		return HAPExpressionSuiteUtility.compileExpression(expression, contextExpressionDefinitions, parentVariablesInfo, contextConstants, expectOutput, configure, processTracker);
	}

	public HAPExecutableExpression compileExpression(
			HAPDefinitionExpression expression, 
			Map<String, HAPVariableInfo> parentVariablesInfo, 
			Map<String, HAPData> contextConstants,
			HAPDataTypeCriteria expectOutput, 
			Map<String, String> configure,
			HAPProcessTracker processTracker) {
		
		return HAPExpressionSuiteUtility.compileExpression(expression, new LinkedHashMap<String, HAPDefinitionExpression>(), parentVariablesInfo, contextConstants, expectOutput, configure, processTracker);
	}


	public HAPExecutableExpression compileExpression(
			HAPDefinitionExpression expression, 
			HAPDefinitionExpressionSuite contextExpressionDefinitionsSuite, 
			HAPDataTypeCriteria expectOutput, 
			Map<String, String> configure,
			HAPProcessTracker processTracker) {
		return this.compileExpression(expression, contextExpressionDefinitionsSuite.getExpressionDefinitions(), contextExpressionDefinitionsSuite.getVariablesInfo(), contextExpressionDefinitionsSuite.getConstants(), expectOutput, configure, processTracker);
	}	
	
}
