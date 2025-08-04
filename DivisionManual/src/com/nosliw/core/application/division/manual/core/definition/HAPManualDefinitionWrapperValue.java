package com.nosliw.core.application.division.manual.core.definition;

import java.util.Map;

import com.nosliw.common.serialization.HAPSerializableImp;

public class HAPManualDefinitionWrapperValue extends HAPSerializableImp{

	public static final String VALUETYPE = "valueType";
	
	private String m_valueType;
	
	public HAPManualDefinitionWrapperValue(String valueType) {
		this.m_valueType = valueType;
	}
	
	public String getValueType() {   return this.m_valueType;  }
	
	@Override
	protected void buildJsonMap(Map<String, String> jsonMap, Map<String, Class<?>> typeJsonMap){
		super.buildJsonMap(jsonMap, typeJsonMap);
		jsonMap.put(VALUETYPE, m_valueType);
	}
}
