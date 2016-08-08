package com.nosliw.expression.utils;

import com.nosliw.data.HAPOperand;

public interface HAPIterateOperandTask{
	public HAPIterateOperandTaskOut process(HAPOperand operand, Object data, boolean isRoot);
}
