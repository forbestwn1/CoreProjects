package com.nosliw.data.core.entity.division.manual;

import com.nosliw.common.utils.HAPConstantShared;

public class HAPManualInfoValueWithValue extends HAPManualInfoValue{

	//entity definition
	public static final String VALUE = "value";

	private Object m_value;
	
	public HAPManualInfoValueWithValue(Object value) {
		super(HAPConstantShared.EMBEDEDVALUE_TYPE_VALUE, null);
		this.m_value = value;
	}

	public Object getValue() {    return this.m_value;     }
	
}
