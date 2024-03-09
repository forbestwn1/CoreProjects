package com.nosliw.data.core.entity.valuestructure;

import java.util.ArrayList;
import java.util.List;

public class HAPInfoPartValueStructure {

	//type of part : default, from parent, ...
	private String m_name;
	
	//priority within value structure complex
	private List<Integer> m_priority;
	
	private HAPInfoPartValueStructure() {
		this.m_priority = new ArrayList<Integer>();
	}
	
	public HAPInfoPartValueStructure(String name, int priority) {
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
	
	public HAPInfoPartValueStructure cloneValueStructurePartInfo() {
		HAPInfoPartValueStructure out = new HAPInfoPartValueStructure();
		out.m_name = this.m_name;
		out.m_priority.addAll(this.m_priority);
		return out;
	}
}
