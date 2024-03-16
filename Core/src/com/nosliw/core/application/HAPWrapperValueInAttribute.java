package com.nosliw.core.application;

import java.util.Map;

import com.nosliw.common.constant.HAPAttribute;
import com.nosliw.common.constant.HAPEntityWithAttribute;
import com.nosliw.data.core.runtime.HAPExecutableImp;

@HAPEntityWithAttribute
public class HAPWrapperValueInAttribute extends HAPExecutableImp{

	@HAPAttribute
	public static final String VALUETYPE = "valueType";
	
	private String m_valueType;
	
	public HAPWrapperValueInAttribute(String valueType) {
		this.m_valueType = valueType;
	}
	
	public String getValueType() {     return this.m_valueType;    }
	
	
	@Override
	protected void buildJsonMap(Map<String, String> jsonMap, Map<String, Class<?>> typeJsonMap){
		super.buildJsonMap(jsonMap, typeJsonMap);
		jsonMap.put(VALUETYPE, this.m_valueType);
	}
	
}
