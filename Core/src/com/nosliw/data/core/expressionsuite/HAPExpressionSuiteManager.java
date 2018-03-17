package com.nosliw.data.core.expressionsuite;

import java.util.LinkedHashMap;
import java.util.Map;

import com.nosliw.common.utils.HAPProcessContext;
import com.nosliw.data.core.HAPData;
import com.nosliw.data.core.criteria.HAPDataTypeCriteria;
import com.nosliw.data.core.expression.HAPDefinitionExpression;
import com.nosliw.data.core.expression.HAPVariableInfo;
import com.nosliw.data.core.runtime.HAPExecuteExpression;

public class HAPExpressionSuiteManager {

	public HAPExpressionSuiteManager() {
	}
	
	public HAPExecuteExpression compileExpression(
			String id,
			HAPDefinitionExpression expression, 
			Map<String, HAPDefinitionExpression> contextExpressionDefinitions, 
			Map<String, HAPVariableInfo> parentVariablesInfo, 
			Map<String, HAPData> contextConstants,
			HAPDataTypeCriteria expectOutput, 
			Map<String, String> configure,
			HAPProcessContext context) {
		
		return HAPExpressionSuiteUtility.compileExpression(id, expression, contextExpressionDefinitions, parentVariablesInfo, contextConstants, expectOutput, configure, context);
	}

	public HAPExecuteExpression compileExpression(
			String id,
			HAPDefinitionExpression expression, 
			Map<String, HAPVariableInfo> parentVariablesInfo, 
			Map<String, HAPData> contextConstants,
			HAPDataTypeCriteria expectOutput, 
			Map<String, String> configure,
			HAPProcessContext context) {
		
		return HAPExpressionSuiteUtility.compileExpression(id, expression, new LinkedHashMap<String, HAPDefinitionExpression>(), parentVariablesInfo, contextConstants, expectOutput, configure, context);
	}


	public HAPExecuteExpression compileExpression(
			String id,
			HAPDefinitionExpression expression, 
			HAPDefinitionExpressionSuite contextExpressionDefinitionsSuite, 
			HAPDataTypeCriteria expectOutput, 
			Map<String, String> configure,
			HAPProcessContext context) {
		return this.compileExpression(id, expression, contextExpressionDefinitionsSuite.getExpressionDefinitions(), contextExpressionDefinitionsSuite.getVariablesInfo(), contextExpressionDefinitionsSuite.getConstants(), expectOutput, configure, context);
	}	
	
}