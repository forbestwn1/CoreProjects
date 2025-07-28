package com.nosliw.core.application;

import java.util.Map;

import com.nosliw.common.constant.HAPAttribute;
import com.nosliw.common.constant.HAPEntityWithAttribute;
import com.nosliw.common.serialization.HAPSerializableImp;
import com.nosliw.core.resource.HAPWithResourceDependency;

@HAPEntityWithAttribute
public abstract class HAPWrapperValue extends HAPSerializableImp implements HAPWithResourceDependency{

	@HAPAttribute
	public static final String VALUETYPE = "valueType";
	
	private String m_valueType;
	
	public HAPWrapperValue(String valueType) {
		this.m_valueType = valueType;
	}
	
	public String getValueType() {     return this.m_valueType;    }

	abstract public Object getValue();
	
	@Override
	protected void buildJsonMap(Map<String, String> jsonMap, Map<String, Class<?>> typeJsonMap){
		super.buildJsonMap(jsonMap, typeJsonMap);
		jsonMap.put(VALUETYPE, this.m_valueType);
	}
	
	@Override
	protected void buildJSJsonMap(Map<String, String> jsonMap, Map<String, Class<?>> typeJsonMap){
		this.buildJsonMap(jsonMap, typeJsonMap);
	}
	
}
