package com.nosliw.core.application.common.dataexpression.definition;

import com.nosliw.common.utils.HAPConstantShared;
import com.nosliw.core.data.HAPData;

public class HAPDefinitionOperandConstant extends HAPDefinitionOperand{

	protected HAPData m_data;

	protected String m_constantStr;

	public HAPDefinitionOperandConstant(String constantStr) {
		super(HAPConstantShared.EXPRESSION_OPERAND_CONSTANT);
		this.m_constantStr = constantStr;
	}

	public String getStringValue(){  return this.m_constantStr;  }

}
