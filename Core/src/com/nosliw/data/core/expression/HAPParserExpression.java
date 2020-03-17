package com.nosliw.data.core.expression;

import com.nosliw.data.core.operand.HAPOperand;

public interface HAPParserExpression {

	  HAPOperand parseExpression(String expression);
}
