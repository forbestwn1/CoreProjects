package com.nosliw.data.core.domain.valueport;

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
		this.m_valuePortByKey.put(valuePort.getValuePortId().getKey(), valuePort);
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
				if(valuePort.isDefault())  return valuePort;
			}
		}
		return null;
	}
	
}
