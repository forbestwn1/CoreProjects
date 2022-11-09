package com.nosliw.data.core.domain.entity.valuestructure;

import java.util.LinkedHashMap;
import java.util.Map;

import com.nosliw.common.serialization.HAPUtilityJson;
import com.nosliw.common.serialization.HAPSerializableImp;
import com.nosliw.common.serialization.HAPSerializationFormat;
import com.nosliw.data.core.domain.HAPDomainValueStructure;

//wrapper for value structure
//extra info for value structure, group name
public class HAPWrapperExecutableValueStructure extends HAPSerializableImp{

	public static final String GROUPNAME = "groupName";
	public static final String GROUPTYPE = "groupType";
	public static final String RUNTIMEID = "runtimeId";
	public static final String DEFINITIONID = "definitionId";
	public static final String VALUESTRUCTURE = "valueStructure";
	
	private String m_groupName;

	//group type for value structure (public, private, protected, internal)
	private String m_groupType;
	
	private String m_valueStructureRuntimeId;
	
	public HAPWrapperExecutableValueStructure() {}

	public HAPWrapperExecutableValueStructure(String valueStructureRuntimeId) {
		this.m_valueStructureRuntimeId = valueStructureRuntimeId;
	}
	
	public String getValueStructureRuntimeId() {	return this.m_valueStructureRuntimeId;	}
	public void setValueStructureRuntimeId(String valueStructureRuntimeId) {	this.m_valueStructureRuntimeId = valueStructureRuntimeId;	}
	
	public String getGroupName() {   return this.m_groupName;   }
	public void setGroupName(String groupName) {   this.m_groupName = groupName;    }
	
	public String getGroupType() {   return this.m_groupType;    }
	public void setGroupType(String groupType) {    this.m_groupType = groupType;     }

	@Override
	protected void buildJsonMap(Map<String, String> jsonMap, Map<String, Class<?>> typeJsonMap){
		jsonMap.put(GROUPTYPE, this.m_groupType);
		jsonMap.put(GROUPNAME, this.m_groupName);
		jsonMap.put(RUNTIMEID, this.m_valueStructureRuntimeId);
	}

	public void cloneFromDefinition(HAPDefinitionWrapperValueStructure valueStructureDefWrapper) {
		this.m_groupName = valueStructureDefWrapper.getGroupName();
		this.m_groupType = valueStructureDefWrapper.getGroupType();
	}
	
	public HAPWrapperExecutableValueStructure cloneValueStructureWrapper() {
		HAPWrapperExecutableValueStructure out = new HAPWrapperExecutableValueStructure();
		out.m_valueStructureRuntimeId = this.m_valueStructureRuntimeId;
		out.m_groupName = this.m_groupName;
		out.m_groupType = this.m_groupType;
		return out;
	}
	
	public String toExpandedString(HAPDomainValueStructure valueStructureDomain) {
		Map<String, String> jsonMap = new LinkedHashMap<String, String>();
		this.buildFullJsonMap(jsonMap, null);
		jsonMap.put(DEFINITIONID, valueStructureDomain.getValueStructureDefinitionIdByRuntimeId(m_valueStructureRuntimeId));
		jsonMap.put(VALUESTRUCTURE, valueStructureDomain.getValueStructureDefInfoByRuntimeId(m_valueStructureRuntimeId).toStringValue(HAPSerializationFormat.JSON));
		return HAPUtilityJson.buildMapJson(jsonMap);
	}
}
