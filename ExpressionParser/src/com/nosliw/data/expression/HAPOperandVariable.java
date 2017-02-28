package com.nosliw.data.expression;

import com.nosliw.data.core.HAPDataTypeCriteria;

public class HAPOperandVariable implements HAPOperand{

	private String m_variableName;
	
	private HAPDataTypeCriteria m_dataTypeCriteria;

	public HAPOperandVariable(String name){
		this.m_variableName = name;
	}
	
}
