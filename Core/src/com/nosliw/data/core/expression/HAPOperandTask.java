package com.nosliw.data.core.expression;

public abstract class HAPOperandTask {

	abstract public boolean processOperand(HAPOperand operand, Object data);

	public void postPross(HAPOperand operand, Object data){}
}
