package com.nosliw.data.core.expression;

import java.util.Map;

import com.nosliw.data.core.criteria.HAPDataTypeCriteria;

/**
 * Expression object we get after processing HAPExpressionInfo
 *  
 */
public interface HAPExpression {

	//ExpressionInfo used to define expression
	HAPExpressionInfo getExpressionInfo();
	
	//Operand to represent the expression
	HAPOperand getOperand();
	
	//Variables infos
	Map<String, HAPDataTypeCriteria> getVariables();

	//error message used to indicate whether the expression is successfully processed
	String getErrorMessage();
}
