package com.nosliw.core.application.division.manual.common.valuecontext;

import java.util.Map;

import com.nosliw.common.serialization.HAPSerializableImp;

public class HAPManualInfoPartInValueContext extends HAPSerializableImp{

	public static final String NAME = "name";
	
	public static final String PRIORITY = "priority";
	
	//type of part : default, from parent, ...
	private String m_name;
	
	//priority within value structure complex
	private int m_priority;
	
	private HAPManualInfoPartInValueContext() {
		this.m_priority = 0;
	}
	
	public HAPManualInfoPartInValueContext(String name, int priority) {
		this.m_name = name;
		this.m_priority = priority;
	}
	
	public String getName() {   return this.m_name;   }

	public int getPriority() {   return this.m_priority;    }
	
	public HAPManualInfoPartInValueContext cloneValueStructurePartInfo() {
		HAPManualInfoPartInValueContext out = new HAPManualInfoPartInValueContext();
		out.m_name = this.m_name;
		out.m_priority = this.m_priority;
		return out;
	}
	
	@Override
	protected void buildJsonMap(Map<String, String> jsonMap, Map<String, Class<?>> typeJsonMap){
		jsonMap.put(NAME, this.m_name);
		jsonMap.put(PRIORITY, this.m_priority+"");
		typeJsonMap.put(PRIORITY, Integer.class);
	}

}
