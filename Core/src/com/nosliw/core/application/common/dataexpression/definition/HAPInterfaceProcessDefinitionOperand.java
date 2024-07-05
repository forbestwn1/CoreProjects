package com.nosliw.core.application.common.dataexpression.definition;

public abstract class HAPInterfaceProcessDefinitionOperand {

	abstract public boolean processOperand(HAPWrapperOperand operand, Object data);

	public void postPross(HAPWrapperOperand operand, Object data){}
}
