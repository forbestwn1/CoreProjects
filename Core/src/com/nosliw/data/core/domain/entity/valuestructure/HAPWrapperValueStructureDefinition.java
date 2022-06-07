package com.nosliw.data.core.domain.entity.valuestructure;

import java.util.LinkedHashMap;
import java.util.Map;

import com.nosliw.common.serialization.HAPJsonUtility;
import com.nosliw.common.serialization.HAPSerializableImp;
import com.nosliw.common.serialization.HAPSerializationFormat;
import com.nosliw.data.core.domain.HAPDomainEntityDefinitionGlobal;
import com.nosliw.data.core.domain.HAPIdEntityInDomain;
import com.nosliw.data.core.domain.HAPUtilityDomain;

//wrapper for value structure
//extra info for value structure, group name
public class HAPWrapperValueStructureDefinition extends HAPSerializableImp{

	public static final String GROUPNAME = "groupName";
	public static final String GROUPTYPE = "groupType";
	public static final String VALUESTRUCTURE = "valueStructure";
	
	private String m_groupName;

	//group type for value structure (public, private, protected, internal)
	private String m_groupType;
	
	private HAPIdEntityInDomain m_valueStructureId;
	
	public HAPWrapperValueStructureDefinition() {}

	public HAPWrapperValueStructureDefinition(HAPIdEntityInDomain valueStructureId) {
		this.m_valueStructureId = valueStructureId;
	}
	
	public HAPIdEntityInDomain getValueStructureId() {	return this.m_valueStructureId;	}
	
	public String getGroupName() {   return this.m_groupName;   }
	public void setGroupName(String groupName) {   this.m_groupName = groupName;    }
	
	public String getGroupType() {   return this.m_groupType;    }
	public void setGroupType(String groupType) {    this.m_groupType = groupType;     }

	@Override
	protected void buildJsonMap(Map<String, String> jsonMap, Map<String, Class<?>> typeJsonMap){
		jsonMap.put(GROUPTYPE, this.m_groupType);
		jsonMap.put(GROUPNAME, this.m_groupName);
		jsonMap.put(VALUESTRUCTURE, this.m_valueStructureId.toStringValue(HAPSerializationFormat.JSON));
	}

	public String toExpandedJsonString(HAPDomainEntityDefinitionGlobal entityDefDomain) {
		Map<String, String> jsonMap = new LinkedHashMap<String, String>();
		jsonMap.put(GROUPTYPE, this.m_groupType);
		jsonMap.put(GROUPNAME, this.m_groupName);
		jsonMap.put(VALUESTRUCTURE, HAPUtilityDomain.getEntityExpandedJsonString(this.m_valueStructureId, entityDefDomain));
		return HAPJsonUtility.buildMapJson(jsonMap);
	}

	public HAPWrapperValueStructureDefinition cloneValueStructureWrapper() {
		HAPWrapperValueStructureDefinition out = new HAPWrapperValueStructureDefinition();
		out.m_groupName = this.m_groupName;
		out.m_groupType = this.m_groupType;
		out.m_valueStructureId = this.m_valueStructureId.cloneIdEntityInDomain();
		return out;
	}
}
