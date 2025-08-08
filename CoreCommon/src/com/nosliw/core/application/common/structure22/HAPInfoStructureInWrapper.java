package com.nosliw.core.application.common.structure22;

import java.util.Map;

import org.json.JSONObject;

import com.nosliw.common.serialization.HAPSerializableImp;

public class HAPInfoStructureInWrapper extends HAPSerializableImp{

	public static final String GROUPTYPE = "groupType";
	public static final String INHERITMODE = "inheritMode";

	private String m_groupType;
	
	private String m_inheritMode;
	
	public String getGroupType() {   return this.m_groupType;     }
	public void setGroupType(String groupType) {    this.m_groupType = groupType;     }
	
	public String getInheritMode() {     return this.m_inheritMode;      }
	public void setInheritMode(String mode) {     this.m_inheritMode = mode;      }

	@Override
	protected boolean buildObjectByJson(Object json){
		JSONObject jsonObj = (JSONObject)json;
		this.m_groupType = (String)jsonObj.opt(GROUPTYPE);
		this.m_inheritMode = (String)jsonObj.opt(INHERITMODE);
		return true;  
	}
	
	public HAPInfoStructureInWrapper cloneValueStructureInfoInWrapper() {
		HAPInfoStructureInWrapper out = new HAPInfoStructureInWrapper();
		out.m_groupType = this.m_groupType;
		out.m_inheritMode = this.m_inheritMode;
		return out;
	}

	@Override
	protected void buildJsonMap(Map<String, String> jsonMap, Map<String, Class<?>> typeJsonMap){
		jsonMap.put(GROUPTYPE, this.m_groupType);
		jsonMap.put(INHERITMODE, this.m_inheritMode);
	}
}
