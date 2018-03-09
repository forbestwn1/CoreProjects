package com.nosliw.data.core.runtime;

import java.util.Map;

import com.nosliw.common.constant.HAPAttribute;
import com.nosliw.common.constant.HAPEntityWithAttribute;
import com.nosliw.data.core.expression.HAPMatchers;
import com.nosliw.data.core.operand.HAPOperandWrapper;

@HAPEntityWithAttribute(baseName="EXPRESSION")
public interface HAPExecuteExpression {

	@HAPAttribute
	public static String OPERAND = "operand";
	
	@HAPAttribute
	public static String VARIABLESMATCHERS = "variablesMatchers";

	//Operand to represent the expression
	HAPOperandWrapper getOperand();

	Map<String, HAPMatchers> getVariableMatchers();
	
}
