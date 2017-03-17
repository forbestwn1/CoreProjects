package com.nosliw.data.core.imp.expression;

import com.nosliw.data.core.expression.HAPOperand;

public interface HAPExpressionTask {

	boolean processOperand(HAPOperand operand, Object data);

	void postPross(HAPOperand operand, Object data);
}
