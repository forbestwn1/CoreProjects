package com.nosliw.data.core.entity;

import com.nosliw.data.core.runtime.HAPExecutableImp;

public class HAPInfoAttributeValue extends HAPExecutableImp{

	private String m_valueType;
	
	public HAPInfoAttributeValue(String valueType) {
		this.m_valueType = valueType;
	}
	
	public String getValueType() {     return this.m_valueType;    }
	
}
