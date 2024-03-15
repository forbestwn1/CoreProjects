package com.nosliw.data.core.entity;

import java.util.Map;

import com.nosliw.common.constant.HAPAttribute;
import com.nosliw.common.serialization.HAPSerializationFormat;
import com.nosliw.common.serialization.HAPSerializeManager;
import com.nosliw.common.utils.HAPConstantShared;

public class HAPInfoAttributeValueValue extends HAPInfoAttributeValue{

	@HAPAttribute
	public static final String VALUE = "value";

	private Object m_value;
	
	public HAPInfoAttributeValueValue(Object value) {
		super(HAPConstantShared.ENTITYATTRIBUTE_VALUETYPE_VALUE);
	}

	public Object getValue() {    return this.m_value;    }
	
	@Override
	protected void buildJsonMap(Map<String, String> jsonMap, Map<String, Class<?>> typeJsonMap){
		super.buildJsonMap(jsonMap, typeJsonMap);
		jsonMap.put(VALUE, HAPSerializeManager.getInstance().toStringValue(m_value, HAPSerializationFormat.JSON));
	}
}
