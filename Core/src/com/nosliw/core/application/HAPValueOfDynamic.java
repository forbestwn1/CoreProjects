package com.nosliw.core.application;

import java.util.Map;

import org.json.JSONObject;

import com.nosliw.common.serialization.HAPSerializableImp;

public class HAPValueOfDynamic extends HAPSerializableImp{

	public static final String INTERFACEID = "interfaceId";
	
	private String m_interfaceId;

	public HAPValueOfDynamic() {}

	public HAPValueOfDynamic(String interfaceId) {
		this.m_interfaceId = interfaceId;
	}

	public String getInterfaceId() {	return this.m_interfaceId;	}
	
	@Override
	protected boolean buildObjectByJson(Object json){
		if(json instanceof String) {
			this.m_interfaceId = (String)json;
		} else if(json instanceof JSONObject) {
			
		}
		
		return true;  
	}
	
	@Override
	protected void buildJsonMap(Map<String, String> jsonMap, Map<String, Class<?>> typeJsonMap){
		jsonMap.put(INTERFACEID, m_interfaceId);
	}
}
