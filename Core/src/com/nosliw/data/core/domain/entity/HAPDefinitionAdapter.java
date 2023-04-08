package com.nosliw.data.core.domain.entity;

public class HAPDefinitionAdapter {

	private String m_valueType;
	
	private Object m_value;
	
	public HAPDefinitionAdapter(String valueType, Object value) {
		this.m_valueType = valueType;
		this.m_value = value;
	}
	
	public String getValueType() {    return this.m_valueType;     }
	
	public Object getValue() {    return this.m_value;     }
	
}
