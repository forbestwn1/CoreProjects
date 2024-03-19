package com.nosliw.core.application;

import com.nosliw.common.utils.HAPConstantShared;

public class HAPWrapperValueInAttributeValue extends HAPWrapperValueInAttribute{

	private Object m_value;
	
	public HAPWrapperValueInAttributeValue(Object value) {
		super(HAPConstantShared.ENTITYATTRIBUTE_VALUETYPE_VALUE);
		this.m_value = value;
	}
 
	@Override
	public Object getValue() {    return this.m_value;    }
	
}
