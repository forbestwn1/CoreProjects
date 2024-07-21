package com.nosliw.common.container;

import java.util.Map;

import com.nosliw.common.constant.HAPAttribute;
import com.nosliw.common.constant.HAPEntityWithAttribute;
import com.nosliw.common.info.HAPEntityInfoImp;
import com.nosliw.common.serialization.HAPManagerSerialize;
import com.nosliw.common.serialization.HAPSerializationFormat;

@HAPEntityWithAttribute
public class HAPItemWrapper extends HAPEntityInfoImp{

	@HAPAttribute
	public static String VALUE = "value";
	
	private Object m_value;
	
	public HAPItemWrapper() {}
	
	public HAPItemWrapper(Object value) {
		this.m_value = value;
	}
	
	public Object getValue() {    return this.m_value;      }
	public void setValue(Object value) {   this.m_value = value;    }
	
	@Override
	protected void buildJSJsonMap(Map<String, String> jsonMap, Map<String, Class<?>> typeJsonMap){
		super.buildJsonMap(jsonMap, typeJsonMap);
		jsonMap.put(VALUE, HAPManagerSerialize.getInstance().toStringValue(m_value, HAPSerializationFormat.JAVASCRIPT));
	}
	
	@Override
	protected void buildJsonMap(Map<String, String> jsonMap, Map<String, Class<?>> typeJsonMap){
		super.buildJsonMap(jsonMap, typeJsonMap);
		jsonMap.put(VALUE, HAPManagerSerialize.getInstance().toStringValue(m_value, HAPSerializationFormat.JSON));
	}
	
}
