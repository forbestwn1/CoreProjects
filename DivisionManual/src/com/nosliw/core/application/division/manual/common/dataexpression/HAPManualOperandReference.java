package com.nosliw.core.application.division.manual.common.dataexpression;

import com.nosliw.common.utils.HAPConstantShared;
import com.nosliw.core.application.common.dataexpression.HAPOperandReference;
import com.nosliw.core.application.common.dataexpression.definition.HAPDefinitionOperandReference;

public class HAPManualOperandReference extends HAPManualOperand implements HAPOperandReference{

	public HAPManualOperandReference(HAPDefinitionOperandReference operandDefinition) {
		super(HAPConstantShared.EXPRESSION_OPERAND_REFERENCE, operandDefinition);
	}

}
