package com.nosliw.data.core.domain.entity.valuestructure;

import java.util.LinkedHashMap;
import java.util.Map;

import com.nosliw.common.serialization.HAPJsonUtility;
import com.nosliw.data.core.domain.HAPDomainEntityDefinition;
import com.nosliw.data.core.domain.HAPIdEntityInDomain;
import com.nosliw.data.core.domain.HAPUtilityDomain;

//wrapper for value structure
//extra info for value structure, group name
public class HAPValueStructureGrouped{

	public static final String GROUPNAME = "groupName";
	public static final String VALUESTRUCTURE = "valueStructure";
	
	//group name for value structure (public, private, protected, internal)
	private String m_groupName;

	private HAPIdEntityInDomain m_valueStructureId;
	
	public HAPValueStructureGrouped(HAPIdEntityInDomain valueStructureId) {
		this.m_valueStructureId = valueStructureId;
	}
	
	public HAPIdEntityInDomain getValueStructureId() {	return this.m_valueStructureId;	}
	
	public String getGroupName() {   return this.m_groupName;   }
	public void setGroupName(String groupName) {   this.m_groupName = groupName;    }

	public String toExpandedJsonString(HAPDomainEntityDefinition entityDefDomain) {
		Map<String, String> jsonMap = new LinkedHashMap<String, String>();
		jsonMap.put(GROUPNAME, this.m_groupName);
		jsonMap.put(VALUESTRUCTURE, HAPUtilityDomain.getEntityExpandedJsonString(this.m_valueStructureId, entityDefDomain));
		return HAPJsonUtility.buildMapJson(jsonMap);
	}

}
