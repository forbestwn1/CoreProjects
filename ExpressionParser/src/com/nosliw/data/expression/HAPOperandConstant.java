package com.nosliw.data.expression;

import com.nosliw.common.utils.HAPConstant;
import com.nosliw.data.core.HAPData;

public class HAPOperandConstant implements HAPOperand{

	protected HAPData m_data;

	protected String m_name;
	
	public HAPOperandConstant(String constantStr){
		
	}
	
	@Override
	public String getType(){	return HAPConstant.EXPRESSION_OPERAND_CONSTANT;}
	
}
