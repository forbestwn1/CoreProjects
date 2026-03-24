package com.nosliw.core.application.common.dataexpression.definition;

public class HAPDefinitionParmInOperationOperand {

	private String m_name;
	
	private HAPDefinitionOperand m_operand;
	
	public HAPDefinitionParmInOperationOperand(String name, HAPDefinitionOperand operand){
		this.m_name = name;
		this.m_operand = operand;
	}
	
	public String getName(){		return this.m_name;	}
	
	public HAPDefinitionOperand getOperand(){  return this.m_operand; }
	
}
