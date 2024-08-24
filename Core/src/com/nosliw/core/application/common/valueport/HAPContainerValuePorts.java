package com.nosliw.core.application.common.valueport;

import java.util.ArrayList;
import java.util.List;

import com.nosliw.common.serialization.HAPSerializableImp;
import com.nosliw.common.utils.HAPUtilityBasic;

public class HAPContainerValuePorts extends HAPSerializableImp{

	private List<HAPGroupValuePorts> m_valuePortGroupByName;

	private String m_defaultName;
	
	private int m_idIndex = 0;
	
	public HAPContainerValuePorts() {
		this.m_valuePortGroupByName = new ArrayList<HAPGroupValuePorts>();
	}
	
	public List<HAPGroupValuePorts> getValuePortGroups(){  return this.m_valuePortGroupByName;  }
	
	public HAPGroupValuePorts getValuePortGroup(String groupId) {
		for(HAPGroupValuePorts valuePortGroup : this.m_valuePortGroupByName) {
			if(groupId.equals(valuePortGroup.getName())) {
				return valuePortGroup;
			}
		}
		return null;
	}
	
	public void addValuePortGroup(HAPGroupValuePorts group, boolean isDefault) {
		HAPGroupValuePorts added = this.addValuePortGroup(group);
		if(isDefault) {
			this.m_defaultName = added.getName();
		}
	}

	public HAPGroupValuePorts addValuePortGroup(HAPGroupValuePorts group) {
		String name = group.getName();
		if(HAPUtilityBasic.isStringEmpty(name)) {
			name = "group" + this.m_idIndex;
			this.m_idIndex++;
			group.setName(name);
		}
		this.m_valuePortGroupByName.add(group);
		return group;
	}

	
	public HAPValuePort getValuePort(HAPIdValuePortInBrick valuePortId) {
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
		
		HAPGroupValuePorts group = this.getValuePortGroup(groupName);
		String valuePortName = valuePortId==null?null:valuePortId.getValuePortName();
		return group.getValuePort(valuePortName);
	}

	public HAPIdValuePortInBrick normalizeValuePortId(HAPIdValuePortInBrick valuePortId, String ioDirection) {
		HAPIdValuePortInBrick out = valuePortId;
		if(out==null) {
			out = new HAPIdValuePortInBrick();
		}
		String valuePortGroupId = out.getValuePortGroup();
		if(valuePortGroupId==null) {
			valuePortGroupId = this.getDefaultGroupName();
			out.setValuePortGroup(valuePortGroupId);
		}
		String valuePortName = out.getValuePortName();
		if(valuePortGroupId!=null&&valuePortName==null) {
			HAPGroupValuePorts valuePortGroup = this.getValuePortGroup(valuePortGroupId);
			if(valuePortName==null) {
				valuePortName = valuePortGroup.getDefaultValuePortName(ioDirection);
				out.setValuePortName(valuePortName);
			}
		}
		return out;
	}
	
	private String getDefaultGroupName() {
		if(this.m_defaultName!=null) {
			return this.m_defaultName;
		} else {
			//use the first one as default
			return this.m_valuePortGroupByName.get(0).getName();
		}
	}
}
