package com.nosliw.data.core.entity.division.manual;

import com.nosliw.common.utils.HAPConstantShared;

public class HAPManualInfoAttributeValueValue extends HAPManualInfoAttributeValue{

	//entity definition
	public static final String VALUE = "value";

	private Object m_value;
	
	public HAPManualInfoAttributeValueValue(Object value) {
		super(HAPConstantShared.EMBEDEDVALUE_TYPE_VALUE, null);
		this.m_value = value;
	}

	public Object getValue() {    return this.m_value;     }
	
}
