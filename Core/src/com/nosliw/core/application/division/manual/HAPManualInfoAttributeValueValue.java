package com.nosliw.core.application.division.manual;

import java.util.Map;

import com.nosliw.common.serialization.HAPSerializationFormat;
import com.nosliw.common.serialization.HAPSerializeManager;
import com.nosliw.common.utils.HAPConstantShared;

public class HAPManualInfoAttributeValueValue extends HAPManualInfoAttributeValue{

	//entity definition
	public static final String VALUE = "value";

	private Object m_value;
	
	public HAPManualInfoAttributeValueValue(Object value) {
		super(HAPConstantShared.EMBEDEDVALUE_TYPE_VALUE);
		this.m_value = value;
	}

	public Object getValue() {    return this.m_value;     }
	
	@Override
	protected void buildJsonMap(Map<String, String> jsonMap, Map<String, Class<?>> typeJsonMap){
		super.buildJsonMap(jsonMap, typeJsonMap);
		jsonMap.put(VALUE, HAPSerializeManager.getInstance().toStringValue(this.m_value, HAPSerializationFormat.JSON));
	}
}
