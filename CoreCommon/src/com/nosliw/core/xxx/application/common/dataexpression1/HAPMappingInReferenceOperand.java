package com.nosliw.core.xxx.application.common.dataexpression1;

public class HAPMappingInReferenceOperand {

	private String m_name;
	
	private HAPOperand m_operand;
	
	public HAPMappingInReferenceOperand(String name, HAPOperand operand){
		this.m_name = name;
		this.m_operand = operand;
	}
	
	public String getName(){		return this.m_name;	}
	
	public HAPOperand getOperand(){  return this.m_operand; }
	
}
