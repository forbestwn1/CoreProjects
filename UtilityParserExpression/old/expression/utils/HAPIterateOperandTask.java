package com.nosliw.data.expression.utils;

import com.nosliw.data.core.expression.HAPOperand;

public interface HAPIterateOperandTask{
	public HAPIterateOperandTaskOut process(HAPOperand operand, Object data, boolean isRoot);
}
