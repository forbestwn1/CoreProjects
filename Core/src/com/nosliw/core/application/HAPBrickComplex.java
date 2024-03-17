package com.nosliw.core.application;

import java.util.Map;

import com.nosliw.common.constant.HAPAttribute;
import com.nosliw.common.serialization.HAPSerializationFormat;
import com.nosliw.core.application.valuecontext.HAPValueContext;

public class HAPBrickComplex extends HAPBrick{

	@HAPAttribute
	public static final String VALUECONTEXT = "valueContext";
	
	private HAPValueContext m_valueContext;

	public void setValueContext(HAPValueContext valueContext) {     this.m_valueContext = valueContext;      }
	public HAPValueContext getValueContext() {    return this.m_valueContext;    }
	
	
	@Override
	protected void buildJsonMap(Map<String, String> jsonMap, Map<String, Class<?>> typeJsonMap){
		super.buildJsonMap(jsonMap, typeJsonMap);
		jsonMap.put(VALUECONTEXT, this.m_valueContext.toStringValue(HAPSerializationFormat.JSON));
	}
}
