package com.nosliw.data.core.expression;

import java.util.Map;

import com.nosliw.data.core.HAPDataTypeCriteria;

/**
 * Expression object we get after processing HAPExpressionInfo
 *  
 */
public interface HAPExpression {

	//ExpressionInfo used to define expression
	HAPExpressionInfo getExpressionInfo();
	
	//Operand to represent the expression
	String getOperand();
	
	//Variables infos
	Map<String, HAPDataTypeCriteria> getVariables();
	
}
