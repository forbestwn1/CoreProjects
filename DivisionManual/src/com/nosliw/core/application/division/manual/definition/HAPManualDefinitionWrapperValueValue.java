package com.nosliw.core.application.division.manual.definition;

import java.util.Map;

import com.nosliw.common.serialization.HAPSerializationFormat;
import com.nosliw.common.serialization.HAPManagerSerialize;
import com.nosliw.common.utils.HAPConstantShared;

public class HAPManualDefinitionWrapperValueValue extends HAPManualDefinitionWrapperValue{

	//entity definition
	public static final String VALUE = "value";

	private Object m_value;
	
	public HAPManualDefinitionWrapperValueValue(Object value) {
		super(HAPConstantShared.EMBEDEDVALUE_TYPE_VALUE);
		this.m_value = value;
	}

	public Object getValue() {    return this.m_value;     }
	
	@Override
	protected void buildJsonMap(Map<String, String> jsonMap, Map<String, Class<?>> typeJsonMap){
		super.buildJsonMap(jsonMap, typeJsonMap);
		jsonMap.put(VALUE, HAPManagerSerialize.getInstance().toStringValue(this.m_value, HAPSerializationFormat.JSON));
	}
}
