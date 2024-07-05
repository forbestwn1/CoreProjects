package com.nosliw.core.application.division.manual.common.dataexpression;

import com.nosliw.common.utils.HAPConstantShared;
import com.nosliw.core.application.common.dataexpression.HAPOperandVariable;
import com.nosliw.core.application.common.dataexpression.definition.HAPDefinitionOperandVariable;
import com.nosliw.core.application.common.valueport.HAPIdElement;

public class HAPManualOperandVariable extends HAPManualOperand implements HAPOperandVariable{

	private HAPIdElement m_variableId;
	
	private String m_variableName;
	
	public HAPManualOperandVariable(HAPDefinitionOperandVariable operandDefinition) {
		super(HAPConstantShared.EXPRESSION_OPERAND_VARIABLE, operandDefinition);
		this.m_variableName = operandDefinition.getVariableName();
	}

	@Override
	public HAPIdElement getVariableId() {    return this.m_variableId;    }

	@Override
	public String getVariableName() {   return this.m_variableName;   }

}
