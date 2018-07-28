package com.nosliw.data.core.flow;

import java.util.Map;
import java.util.Set;

import com.nosliw.common.constant.HAPAttribute;
import com.nosliw.common.serialization.HAPSerializableImp;

public abstract class HAPDefinitionStep extends HAPSerializableImp{

	@HAPAttribute
	public static String TYPE = "type";

	@HAPAttribute
	public static String NAME = "name";

	private String m_name;

	abstract public String getType();

	abstract public Set<String> getReferenceNames();
	
	public String getName(){  return this.m_name;  }
	public void setName(String name) {
		this.m_name = name;   
	}
	
	@Override
	protected void buildJsonMap(Map<String, String> jsonMap, Map<String, Class<?>> typeJsonMap) {
		super.buildJsonMap(jsonMap, typeJsonMap);
		jsonMap.put(TYPE, this.getType());
	}
	
}
