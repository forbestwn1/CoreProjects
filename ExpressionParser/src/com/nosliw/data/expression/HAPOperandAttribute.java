package com.nosliw.data.expression;

import com.nosliw.common.utils.HAPConstant;

public class HAPOperandAttribute implements HAPOperand{

	private String m_attribute;
	
	private HAPOperand m_base;
	
	public HAPOperandAttribute(HAPOperand base, String attribute){
		this.m_base = base;
		this.m_attribute = attribute;
	}
	
	@Override
	public String getType(){	return HAPConstant.EXPRESSION_OPERAND_ATTRIBUTEOPERATION;}

}
