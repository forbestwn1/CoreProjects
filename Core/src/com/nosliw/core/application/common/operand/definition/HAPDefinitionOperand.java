package com.nosliw.core.application.common.operand.definition;

public abstract class HAPDefinitionOperand {

	private String m_type;
	
	public HAPDefinitionOperand(String type) {
		this.m_type = type;
	}
	
	public String getType(){ return this.m_type;  }

}
