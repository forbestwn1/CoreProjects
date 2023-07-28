package com.nosliw.data.core.domain.entity.expression.data;

import com.nosliw.data.core.operand.HAPOperand;

public interface HAPParserExpression {

	  HAPOperand parseExpression(String expression);
}
