package com.nosliw.core.application.valueport;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import com.nosliw.common.constant.HAPAttribute;
import com.nosliw.common.constant.HAPEntityWithAttribute;
import com.nosliw.common.serialization.HAPManagerSerialize;
import com.nosliw.common.serialization.HAPSerializableImp;
import com.nosliw.common.serialization.HAPSerializationFormat;
import com.nosliw.common.utils.HAPUtilityBasic;

@HAPEntityWithAttribute
public class HAPContainerValuePorts extends HAPSerializableImp{

	@HAPAttribute
	public static String VALUEPORTGROUP = "valuePortGroup";
	
	private List<HAPGroupValuePorts> m_valuePortGroups;

	private int m_idIndex = 0;
	
	public HAPContainerValuePorts() {
		this.m_valuePortGroups = new ArrayList<HAPGroupValuePorts>();
	}
	 
	public List<HAPGroupValuePorts> getValuePortGroups(){  return this.m_valuePortGroups;  }
	
	//either match with group name or group type
	public HAPGroupValuePorts getValuePortGroup(String group) {
		HAPGroupValuePorts out = null;
		out = this.getValuePortGroupById(group);
		if(out==null) {
			out = this.getValuePortGroupByType(group);
		}
		return out;
	}
	
	public HAPGroupValuePorts getValuePortGroupByType(String groupType) {
		for(HAPGroupValuePorts valuePortGroup : this.m_valuePortGroups) {
			if(groupType.equals(valuePortGroup.getGroupType())) {
				return valuePortGroup;
			}
		}
		return null;
	}
	
	public HAPGroupValuePorts getValuePortGroupById(String groupId) {
		for(HAPGroupValuePorts valuePortGroup : this.m_valuePortGroups) {
			if(groupId.equals(valuePortGroup.getName())) {
				return valuePortGroup;
			}
		}
		return null;
	}
	
	public HAPGroupValuePorts addValuePortGroup(HAPGroupValuePorts group) {
		String name = group.getName();
		if(HAPUtilityBasic.isStringEmpty(name)) {
			name = "group" + this.m_idIndex;
			this.m_idIndex++;
			group.setName(name);
		}
		this.m_valuePortGroups.add(group);
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
	
	public void cleanValueStucture(Set<String> valueStrucutreIds) {
		this.m_valuePortGroups = this.m_valuePortGroups.stream().map(group->{
			group.cleanValueStucture(valueStrucutreIds);
			return group;
		}).filter(group->!group.isEmpty()).collect(Collectors.toList());
	}
	
	private String getDefaultGroupName() {
		//use the first one as default
		return this.m_valuePortGroups.get(0).getName();
	}
	
	@Override
	protected void buildJsonMap(Map<String, String> jsonMap, Map<String, Class<?>> typeJsonMap){
		super.buildJsonMap(jsonMap, typeJsonMap);
		jsonMap.put(VALUEPORTGROUP, HAPManagerSerialize.getInstance().toStringValue(m_valuePortGroups, HAPSerializationFormat.JSON));
	}
}
