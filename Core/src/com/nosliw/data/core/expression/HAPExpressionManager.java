package com.nosliw.data.core.expression;

public interface HAPExpressionManager {

	HAPExpressionInfo getExpressionInfo(String name);
	
	HAPExpression processExpressionInfo(HAPExpressionInfo expressionInfo);
	
}
