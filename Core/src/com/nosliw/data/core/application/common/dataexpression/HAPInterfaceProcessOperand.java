package com.nosliw.data.core.application.common.dataexpression;

public abstract class HAPInterfaceProcessOperand {

	abstract public boolean processOperand(HAPWrapperOperand operand, Object data);

	public void postPross(HAPWrapperOperand operand, Object data){}
}
