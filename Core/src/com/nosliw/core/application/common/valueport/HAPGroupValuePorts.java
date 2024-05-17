package com.nosliw.core.application.common.valueport;

import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;

import com.nosliw.common.info.HAPEntityInfoImp;
import com.nosliw.common.utils.HAPUtilityBasic;

public class HAPGroupValuePorts extends HAPEntityInfoImp{

	private Map<String, HAPValuePort> m_valuePortByName;
	
	private String m_defaultName;
	
	private int m_idIndex = 0;
	
	public HAPGroupValuePorts() {
		this.m_valuePortByName = new LinkedHashMap<String, HAPValuePort>();
	}
	
	public void addValuePort(HAPValuePort valuePort, boolean isDefault) {
		String name = valuePort.getName();
		if(HAPUtilityBasic.isStringEmpty(name)) {
			name = this.m_idIndex + "";
			this.m_idIndex++;
			valuePort.setName(name);
		}
		this.m_valuePortByName.put(name, valuePort);
		if(isDefault) {
			this.m_defaultName = name;
		}
	}
	
	public Set<HAPValuePort> getValuePorts(){
		return new HashSet<HAPValuePort>(this.m_valuePortByName.values());
	}
	
	public HAPValuePort getValuePort(String name) {
		if(HAPUtilityBasic.isStringEmpty(name)) {
			name = this.getDefaultValuePortName();
		}
		return this.m_valuePortByName.get(name);
	}
	
	public String getDefaultValuePortName() {   return this.m_defaultName;  }
}
