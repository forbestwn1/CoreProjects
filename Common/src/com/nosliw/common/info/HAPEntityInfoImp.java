package com.nosliw.common.info;

import java.util.Map;

import org.json.JSONObject;

import com.nosliw.common.serialization.HAPJsonUtility;
import com.nosliw.common.serialization.HAPSerializableImp;
import com.nosliw.common.serialization.HAPSerializationFormat;

public class HAPEntityInfoImp extends HAPSerializableImp implements HAPEntityInfo{

	//name, for display
	private String m_name;

	//description
	private String m_description;
	
	private HAPInfo m_info;

	public HAPEntityInfoImp() {
		this.m_info = new HAPInfoImpSimple(); 
	}
	public HAPEntityInfoImp(String name, String description) {
		this.m_info = new HAPInfoImpSimple(); 
		this.m_name = name;
		this.m_description = description;
	}
	
	@Override
	public String getName() {  return this.m_name;   }

	@Override
	public String getDescription() {   return this.m_description;   }
	
	@Override
	public HAPInfo getInfo() {  return this.m_info;  }

	public Object getInfoValue(String name) {  return this.m_info.getValue(name);   }
	
	@Override
	public void setInfo(HAPInfo info) {  this.m_info = info.cloneInfo();  }
	
	@Override
	public void setName(String name) {  this.m_name = name;    }

	@Override
	public void setDescription(String description) {   this.m_description = description;   }

	public HAPEntityInfoImp clone() {
		HAPEntityInfoImp out = new HAPEntityInfoImp();
		this.cloneToEntityInfo(out);
		return out;
	}
	
	public void cloneToEntityInfo(HAPEntityInfo entityInfo) {
		entityInfo.setInfo(this.m_info.cloneInfo());
		entityInfo.setName(this.m_name);
		entityInfo.setDescription(this.m_description);
	}
	
	@Override
	protected void buildJsonMap(Map<String, String> jsonMap, Map<String, Class<?>> typeJsonMap){
		super.buildJsonMap(jsonMap, typeJsonMap);
		jsonMap.put(NAME, this.m_name);
		jsonMap.put(DESCRIPTION, this.m_description);
		jsonMap.put(INFO, HAPJsonUtility.buildJson(this.m_info, HAPSerializationFormat.JSON));
	}

	@Override
	protected boolean buildObjectByJson(Object json){
		JSONObject jsonObj = (JSONObject)json;
		this.m_name = jsonObj.optString(NAME);
		this.m_description = jsonObj.optString(DESCRIPTION);
		this.m_info = new HAPInfoImpSimple();
		this.m_info.buildObject(jsonObj.optJSONObject(INFO), HAPSerializationFormat.JSON);
		return true;  
	}
}
