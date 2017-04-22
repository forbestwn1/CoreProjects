package com.nosliw.data.core.expression;

public interface HAPExpressionManager {

	void registerExpressionInfo(HAPExpressionInfo expressionInfo);
	
	HAPExpressionInfo getExpressionInfo(String name);
	
	HAPExpression getExpression(String expressionName);
	
}
