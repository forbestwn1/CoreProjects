package com.nosliw.core.application.division.manual.common.valuecontext22;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.nosliw.common.serialization.HAPSerializableImp;

public class HAPManualInfoPartInValueContext extends HAPSerializableImp{

	public static final String NAME = "name";
	
	public static final String PRIORITY = "priority";
	
	//type of part : default, from parent, ...
	private String m_name;
	
	//priority within value structure complex
	private List<Integer> m_priority;
	
	private HAPManualInfoPartInValueContext() {
		this.m_priority = new ArrayList<Integer>();
	}
	
	public HAPManualInfoPartInValueContext(String name, int priority) {
		this.m_name = name;
		this.m_priority = new ArrayList<Integer>();
		this.m_priority.add(priority);
	}
	
	public String getName() {   return this.m_name;   }

	public List<Integer> getPriority() {   return this.m_priority;    }
	
	public void appendParentInfo(List<Integer> rootPriority) {
		List<Integer> priority = new ArrayList<Integer>();
		priority.addAll(rootPriority);
		priority.addAll(this.m_priority);
		this.m_priority = priority;
	}
	
	public HAPManualInfoPartInValueContext cloneValueStructurePartInfo() {
		HAPManualInfoPartInValueContext out = new HAPManualInfoPartInValueContext();
		out.m_name = this.m_name;
		out.m_priority.addAll(this.m_priority);
		return out;
	}
	
	@Override
	protected void buildJsonMap(Map<String, String> jsonMap, Map<String, Class<?>> typeJsonMap){
		jsonMap.put(NAME, this.m_name);
		if(!this.m_priority.isEmpty()) {
			jsonMap.put(PRIORITY, this.m_priority.get(this.m_priority.size()-1)+"");
		}
	}

}
