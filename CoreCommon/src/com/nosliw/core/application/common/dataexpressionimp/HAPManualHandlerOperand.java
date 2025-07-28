package com.nosliw.core.application.common.dataexpressionimp;

public abstract class HAPManualHandlerOperand {

	abstract public boolean processOperand(HAPManualWrapperOperand operandWrapper, Object data);

	public void postPross(HAPManualWrapperOperand operandWrapper, Object data){}
	
}
