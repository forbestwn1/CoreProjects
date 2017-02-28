package com.nosliw.data.expression;

public class HAPOperandAttribute implements HAPOperand{

	private String m_attribute;
	
	public HAPOperandAttribute(HAPOperand baseData, String attribute){
		this.m_attribute = attribute;
	}
}
