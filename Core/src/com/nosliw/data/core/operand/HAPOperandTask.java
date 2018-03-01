package com.nosliw.data.core.operand;

public abstract class HAPOperandTask {

	abstract public boolean processOperand(HAPOperandWrapper operand, Object data);

	public void postPross(HAPOperandWrapper operand, Object data){}
}
