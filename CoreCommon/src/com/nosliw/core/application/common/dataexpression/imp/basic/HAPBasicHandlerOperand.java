package com.nosliw.core.application.common.dataexpression.imp.basic;

public abstract class HAPBasicHandlerOperand {

	abstract public boolean processOperand(HAPBasicWrapperOperand operandWrapper, Object data);

	public void postPross(HAPBasicWrapperOperand operandWrapper, Object data){}
	
}
