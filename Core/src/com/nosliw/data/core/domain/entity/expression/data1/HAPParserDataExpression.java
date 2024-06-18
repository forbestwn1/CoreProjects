package com.nosliw.data.core.domain.entity.expression.data1;

import com.nosliw.data.core.application.common.dataexpression.HAPOperand;

public interface HAPParserDataExpression {

	  HAPOperand parseExpression(String expression);
}
