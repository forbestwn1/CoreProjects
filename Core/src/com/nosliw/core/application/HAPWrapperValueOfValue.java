package com.nosliw.core.application;

import java.util.Map;

import com.nosliw.common.constant.HAPAttribute;
import com.nosliw.common.serialization.HAPSerializationFormat;
import com.nosliw.common.serialization.HAPSerializeManager;
import com.nosliw.common.utils.HAPConstantShared;

public class HAPWrapperValueOfValue extends HAPWrapperValue{

	@HAPAttribute
	public static final String VALUE = "value";
	
	private Object m_value;
	
	public HAPWrapperValueOfValue(Object value) {
		super(HAPConstantShared.ENTITYATTRIBUTE_VALUETYPE_VALUE);
		this.m_value = value;
	}
 
	@Override
	public Object getValue() {    return this.m_value;    }
	
	@Override
	protected void buildJsonMap(Map<String, String> jsonMap, Map<String, Class<?>> typeJsonMap){
		super.buildJsonMap(jsonMap, typeJsonMap);
		jsonMap.put(VALUE, HAPSerializeManager.getInstance().toStringValue(this.m_value, HAPSerializationFormat.JSON));
	}

	@Override
	protected void buildJSJsonMap(Map<String, String> jsonMap, Map<String, Class<?>> typeJsonMap){
		this.buildJsonMap(jsonMap, typeJsonMap);
		jsonMap.put(VALUE, HAPSerializeManager.getInstance().toStringValue(this.m_value, HAPSerializationFormat.JAVASCRIPT));
	}
}
