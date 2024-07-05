package com.nosliw.core.application.common.dataexpression.definition;

import com.nosliw.common.utils.HAPConstantShared;

public class HAPDefinitionOperandAttribute extends HAPDefinitionOperand{

	private String m_attribute;
	
	private HAPDefinitionOperand m_base;
	
	public HAPDefinitionOperandAttribute(HAPDefinitionOperand base, String attribute){
		super(HAPConstantShared.EXPRESSION_OPERAND_ATTRIBUTEOPERATION);
		this.setBase(base);
		this.m_attribute = attribute;
	}
	
	public String getAttribute(){   return this.m_attribute;   }
	
	public HAPDefinitionOperand getBase() {  return this.m_base;  }
	
	public void setBase(HAPDefinitionOperand base){	this.m_base = base;	}
	
}
