package com.nosliw.data.core.expression;

public interface HAPExpressionTask {

	boolean processOperand(HAPOperand operand, Object data);

	void postPross(HAPOperand operand, Object data);
}
