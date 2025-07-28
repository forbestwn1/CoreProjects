package com.nosliw.core.application.common.dataexpression.definition;

import com.nosliw.common.utils.HAPConstantShared;

public class HAPDefinitionOperandVariable extends HAPDefinitionOperand{

	protected String m_variableName;

	public HAPDefinitionOperandVariable(String name){
		super(HAPConstantShared.EXPRESSION_OPERAND_VARIABLE);
		this.m_variableName = name;
	}

	public String getVariableName(){  return this.m_variableName;  }
	public void setVariableName(String name){   this.m_variableName = name;  }

}
