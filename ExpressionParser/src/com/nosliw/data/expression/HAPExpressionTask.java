package com.nosliw.data.expression;

import com.nosliw.data.core.expression.HAPOperand;

public interface HAPExpressionTask {

	boolean processOperand(HAPOperand operand, Object data);
	
}
