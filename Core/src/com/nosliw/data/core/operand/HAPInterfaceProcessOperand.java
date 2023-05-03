package com.nosliw.data.core.operand;

public abstract class HAPInterfaceProcessOperand {

	abstract public boolean processOperand(HAPWrapperOperand operand, Object data);

	public void postPross(HAPWrapperOperand operand, Object data){}
}
