package com.nosliw.core.application.common.structure;

import java.util.Map;

import org.json.JSONObject;

import com.nosliw.common.serialization.HAPSerializableImp;

public class HAPInfoStructureInWrapper extends HAPSerializableImp{

	public static final String SCOPE = "scope";
	public static final String INHERITMODE = "inheritMode";

	private String m_scope;
	
	private String m_inheritMode;
	
	public String getScope() {   return this.m_scope;     }
	public void setScope(String scope) {    this.m_scope = scope;     }
	
	public String getInheritMode() {     return this.m_inheritMode;      }
	public void setInheritMode(String mode) {     this.m_inheritMode = mode;      }

	@Override
	protected boolean buildObjectByJson(Object json){
		JSONObject jsonObj = (JSONObject)json;
		this.m_scope = (String)jsonObj.opt(SCOPE);
		this.m_inheritMode = (String)jsonObj.opt(INHERITMODE);
		return true;  
	}
	
	public HAPInfoStructureInWrapper cloneValueStructureInfoInWrapper() {
		HAPInfoStructureInWrapper out = new HAPInfoStructureInWrapper();
		out.m_scope = this.m_scope;
		out.m_inheritMode = this.m_inheritMode;
		return out;
	}

	@Override
	protected void buildJsonMap(Map<String, String> jsonMap, Map<String, Class<?>> typeJsonMap){
		jsonMap.put(SCOPE, this.m_scope);
		jsonMap.put(INHERITMODE, this.m_inheritMode);
	}
}
