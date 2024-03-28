package com.nosliw.core.application.common.valueport;

import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;

public class HAPContainerValuePorts {

	private Map<String, HAPValuePort> m_valuePortByKey;
	
	public HAPContainerValuePorts() {
		this.m_valuePortByKey = new LinkedHashMap<String, HAPValuePort>();
	}
	
	public void addValuePort(HAPValuePort valuePort) {
		String key = getValuePortKey(valuePort);
		this.m_valuePortByKey.put(key, valuePort);
	}
	
	public void addValuePorts(HAPContainerValuePorts valuePortContainer) {
		if(valuePortContainer!=null) {
			for(HAPValuePort valuePort : valuePortContainer.getValuePorts()) {
				this.addValuePort(valuePort);
			}
		}
	}
	
	public Set<HAPValuePort> getValuePorts(){
		return new HashSet<HAPValuePort>(this.m_valuePortByKey.values());
	}
	
	public HAPValuePort getValuePort(HAPIdValuePort valuePortId) {
		if(valuePortId!=null) {
			return this.m_valuePortByKey.get(valuePortId.getKey());
		}
		else {
			//find default one
			for(HAPValuePort valuePort : this.m_valuePortByKey.values()) {
				if(valuePort.isDefault()) {
					return valuePort;
				}
			}
		}
		return null;
	}
	
	public HAPIdValuePort getDefaultValuePortId() {
		for(String key : this.m_valuePortByKey.keySet()) {
			if(this.m_valuePortByKey.get(key).isDefault()) {
				return new HAPIdValuePort(key);
			}
		}
		return null;
	}
	
	private String getValuePortKey(HAPValuePort valuePort) {
		return new HAPIdValuePort(valuePort.getValuePortInfo().getType(), valuePort.getName()).getKey();
	}
}
