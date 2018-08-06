package com.nosliw.data.core.flow;

import java.util.Map;

import com.nosliw.common.constant.HAPAttribute;
import com.nosliw.common.serialization.HAPSerializableImp;

public abstract class HAPDefinitionStep extends HAPSerializableImp{

	@HAPAttribute
	public static String TYPE = "type";

	@HAPAttribute
	public static String ID = "id";
	
	@HAPAttribute
	public static String NAME = "name";

	@HAPAttribute
	public static String DESCRIPTION = "description";
	
	//unique id for each step
	private String m_id;

	//for display purpose only
	private String m_name;

	//for display purpose only
	private String m_description;

	public HAPDefinitionStep(String id) {
		this.m_id = id;
	}
	
	abstract public String getType();

	public String getName(){  return this.m_name;  }
	public void setName(String name) {	this.m_name = name;	}
	
	@Override
	protected void buildJsonMap(Map<String, String> jsonMap, Map<String, Class<?>> typeJsonMap) {
		super.buildJsonMap(jsonMap, typeJsonMap);
		jsonMap.put(TYPE, this.getType());
		jsonMap.put(ID, this.m_id);
		jsonMap.put(NAME, this.m_name);
		jsonMap.put(DESCRIPTION, this.m_description);
	}
	
}
