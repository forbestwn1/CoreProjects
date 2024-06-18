package com.nosliw.data.core.application.common.dataexpression;

public class HAPParmInOperationOperand {

	private String m_name;
	
	private HAPOperand m_operand;
	
	public HAPParmInOperationOperand(String name, HAPOperand operand){
		this.m_name = name;
		this.m_operand = operand;
	}
	
	public String getName(){		return this.m_name;	}
	
	public HAPOperand getOperand(){  return this.m_operand; }
	
}
