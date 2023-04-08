package com.nosliw.data.core.domain.entity;

import com.nosliw.data.core.runtime.HAPExecutableImp;

public class HAPExecutableAdapter extends HAPExecutableImp{

	private String m_valueType;
	
	private Object m_value;
	
	public HAPExecutableAdapter(String valueType, Object value) {
		this.m_valueType = valueType;
		this.m_value = value;
	}
	
	public String getValueType() {    return this.m_valueType;     }
	
	public Object getValue() {    return this.m_value;     }
	
}
