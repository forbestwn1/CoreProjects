package com.nosliw.data.core.expression;

public interface HAPExpressionManager {

	void registerExpressionDefinition(HAPExpressionDefinition expressionDefinition);
	
	HAPExpressionDefinition getExpressionDefinition(String name);
	
	HAPExpression getExpression(String expressionName);
	
}
