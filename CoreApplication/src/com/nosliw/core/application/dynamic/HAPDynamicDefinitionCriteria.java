package com.nosliw.core.application.dynamic;

import java.util.Map;

import com.nosliw.common.serialization.HAPSerializableImp;

public class HAPDynamicDefinitionCriteria extends HAPSerializableImp{

	public final static String STRUCTURE = "structure"; 

	private String m_structure;
	
	public HAPDynamicDefinitionCriteria(String structure) {
		this.m_structure = structure;
	}
	
	public String getStructure() {	return this.m_structure;	}

	@Override
	protected void buildJsonMap(Map<String, String> jsonMap, Map<String, Class<?>> typeJsonMap){
		super.buildJsonMap(jsonMap, typeJsonMap);
		jsonMap.put(STRUCTURE, this.m_structure);
	}
}
