package com.nosliw.data.core.expression;

public interface HAPExpressionManager {

	void registerExpressionInfo(String name, HAPExpressionInfo expressionInfo);
	
	HAPExpressionInfo getExpressionInfo(String name);
	
	HAPExpression processExpressionInfo(String expressionName);
	
}
