package com.nosliw.core.application.division.manual.common.valuecontext;

import java.util.Map;

import com.nosliw.common.serialization.HAPSerializableImp;

//wrapper for value structure
//extra info for value structure, group name
public class HAPManualInfoValueStructure extends HAPSerializableImp{

	public static final String GROUPTYPE = "groupType";
	public static final String RUNTIMEID = "runtimeId";
	public static final String INHERITMODE = "inheritMode";

	//group type for value structure (public, private, protected, internal)
	private String m_groupType;
	
	private String m_inheritMode;
	
	private String m_valueStructureRuntimeId;
	
	public HAPManualInfoValueStructure() {}

	public HAPManualInfoValueStructure(String valueStructureRuntimeId) {
		this.m_valueStructureRuntimeId = valueStructureRuntimeId;
	}
	
	public String getValueStructureRuntimeId() {	return this.m_valueStructureRuntimeId;	}
	public void setValueStructureRuntimeId(String valueStructureRuntimeId) {	this.m_valueStructureRuntimeId = valueStructureRuntimeId;	}
	
	public String getGroupType() {   return this.m_groupType;    }
	public void setGroupType(String groupType) {    this.m_groupType = groupType;     }

	public String getInheritMode() {    return this.m_inheritMode;    }
	public void setInheritMode(String mode) {    this.m_inheritMode = mode;      }
	
	@Override
	protected void buildJsonMap(Map<String, String> jsonMap, Map<String, Class<?>> typeJsonMap){
		jsonMap.put(GROUPTYPE, this.m_groupType);
		jsonMap.put(RUNTIMEID, this.m_valueStructureRuntimeId);
		if(this.m_inheritMode!=null) {
			jsonMap.put(INHERITMODE, m_inheritMode);
		}
	}

	public HAPManualInfoValueStructure cloneValueStructureWrapper() {
		HAPManualInfoValueStructure out = new HAPManualInfoValueStructure();
		out.m_valueStructureRuntimeId = this.m_valueStructureRuntimeId;
		out.m_groupType = this.m_groupType;
		out.m_inheritMode = this.m_inheritMode;
		return out;
	}
	
	public void cloneToChildValueStructureWrapper(HAPManualInfoValueStructure valueStructureInfo) {
		valueStructureInfo.m_groupType = this.m_groupType;
	}
	
}
