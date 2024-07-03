package com.nosliw.data.core.domain.entity.expression.data1;

import com.nosliw.core.application.common.operand.definition.HAPDefinitionOperand;

public interface HAPParserDataExpression {

	  HAPDefinitionOperand parseExpression(String expression);
}
