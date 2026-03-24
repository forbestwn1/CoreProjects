package com.nosliw.core.application.entity.brickcriteria;

import java.util.Map;

import com.nosliw.common.serialization.HAPSerializableImp;

public class HAPRestrainBrick extends HAPSerializableImp{

	public final static String TYPE = "type"; 

	private String m_type;
	
	public HAPRestrainBrick(String type) {
		this.m_type = type;
	}
	
	public String getType() {    return this.m_type;	}
	
//	String[] isValid(HAPBrick brick);
	
	@Override
	protected void buildJsonMap(Map<String, String> jsonMap, Map<String, Class<?>> typeJsonMap){
		super.buildJsonMap(jsonMap, typeJsonMap);
		jsonMap.put(TYPE, this.m_type);
	}
}
