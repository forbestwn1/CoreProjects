package com.nosliw.core.application.common.dataexpression;

import com.nosliw.core.application.common.dataexpression.definition.HAPDefinitionOperand;

public abstract class HAPInterfaceProcessOperand {

	abstract public boolean processOperand(HAPDefinitionOperand operand, Object data);

	public void postPross(HAPDefinitionOperand operand, Object data){}
}
