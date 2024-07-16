package com.nosliw.core.application.division.manual.common.dataexpression1;

public abstract class HAPManualHandlerOperand {

	abstract public boolean processOperand(HAPManualWrapperOperand operandWrapper, Object data);

	public void postPross(HAPManualWrapperOperand operandWrapper, Object data){}
	
}
