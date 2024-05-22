package com.nosliw.core.application.common.valueport;

import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;

import com.nosliw.common.utils.HAPUtilityBasic;

public class HAPContainerValuePorts {

	private Map<String, HAPGroupValuePorts> m_valuePortGroupByName;

	private String m_defaultName;
	
	private int m_idIndex = 0;
	
	public HAPContainerValuePorts() {
		this.m_valuePortGroupByName = new LinkedHashMap<String, HAPGroupValuePorts>();
	}
	
	public Set<HAPGroupValuePorts> getValuePortGroups(){
		return new HashSet<HAPGroupValuePorts>(this.m_valuePortGroupByName.values());
	}
	
	public HAPGroupValuePorts getValuePortGroup(String groupId) {
		return this.m_valuePortGroupByName.get(groupId);
	}
	
	public void addValuePortGroup(HAPGroupValuePorts group, boolean isDefault) {
		String name = group.getName();
		if(HAPUtilityBasic.isStringEmpty(name)) {
			name = "group" + this.m_idIndex;
			this.m_idIndex++;
			group.setName(name);
		}
		this.m_valuePortGroupByName.put(group.getName(), group);
		if(isDefault) {
			this.m_defaultName = name;
		}
	}

	public HAPIdValuePort getDefaultValuePortId() {
		String groupId = this.getDefaultGroupName();
		String valuePortId = null;
		if(groupId!=null) {
			valuePortId = this.m_valuePortGroupByName.get(groupId).getDefaultValuePortName();
		}
		return new HAPIdValuePort(groupId, valuePortId);
	}
	
	public HAPValuePort getValuePort(HAPIdValuePort valuePortId) {
		String groupName = null;
		if(valuePortId==null||valuePortId.getValuePortGroup()==null) {
			groupName = this.getDefaultGroupName();
		}
		else {
			groupName = valuePortId.getValuePortGroup();
		}
		if(groupName==null) {
			return null;
		}
		
		HAPGroupValuePorts group = this.m_valuePortGroupByName.get(groupName);
		String valuePortName = valuePortId==null?null:valuePortId.getValuePortName();
		return group.getValuePort(valuePortName);
	}

	public HAPIdValuePort normalizeValuePortId(HAPIdValuePort valuePortId) {
		HAPIdValuePort out = valuePortId;
		if(out==null) {
			out = new HAPIdValuePort();
		}
		String valuePortGroupId = out.getValuePortGroup();
		if(valuePortGroupId==null) {
			valuePortGroupId = this.getDefaultGroupName();
			out.setValuePortGroup(valuePortGroupId);
		}
		String valuePortName = out.getValuePortName();
		if(valuePortGroupId!=null) {
			HAPGroupValuePorts valuePortGroup = this.getValuePortGroup(valuePortGroupId);
			if(valuePortName==null) {
				valuePortName = valuePortGroup.getDefaultValuePortName();
				out.setValuePortName(valuePortName);
			}
		}
		return out;
	}
	
	private String getDefaultGroupName() {    return this.m_defaultName;    }
}
