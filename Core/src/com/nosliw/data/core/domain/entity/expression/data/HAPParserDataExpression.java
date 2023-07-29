package com.nosliw.data.core.domain.entity.expression.data;

import com.nosliw.data.core.operand.HAPOperand;

public interface HAPParserDataExpression {

	  HAPOperand parseExpression(String expression);
}
