package com.nosliw.data.core.expression;

import com.nosliw.common.utils.HAPConstant;
import com.nosliw.data.core.HAPDataTypeCriteria;

public class HAPOperandVariable implements HAPOperand{

	protected String m_variableName;
	
	protected HAPDataTypeCriteria m_dataTypeCriteria;

	public HAPOperandVariable(String name){
		this.m_variableName = name;
	}
	
	@Override
	public String getType(){	return HAPConstant.EXPRESSION_OPERAND_VARIABLE;}
}
