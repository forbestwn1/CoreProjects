package com.nosliw.data.core.expression;

import com.nosliw.data.core.operand.HAPOperand;

public interface HAPExpressionParser {

	  HAPOperand parseExpression(String expression);
}
