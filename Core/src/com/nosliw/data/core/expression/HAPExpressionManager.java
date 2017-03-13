package com.nosliw.data.core.expression;

public interface HAPExpressionManager {

	public static final String SIGN_MODIFYED = "AAAAAAAAAAAAAAAAAA";
	
	HAPExpressionInfo getExpressionInfo(String name);
	
	HAPExpression processExpressionInfo(HAPExpressionInfo expressionInfo);
	
}
