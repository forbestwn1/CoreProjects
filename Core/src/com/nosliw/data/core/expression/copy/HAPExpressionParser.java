package com.nosliw.data.core.expression.copy;

import com.nosliw.data.core.operand.HAPOperand;

public interface HAPExpressionParser {

	  HAPOperand parseExpression(String expression);
}
