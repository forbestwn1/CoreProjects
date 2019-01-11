package com.nosliw.common.info;

import java.util.Map;

import org.json.JSONObject;

import com.nosliw.common.serialization.HAPJsonUtility;
import com.nosliw.common.serialization.HAPSerializableImp;
import com.nosliw.common.serialization.HAPSerializationFormat;

public class HAPEntityInfoImp extends HAPSerializableImp implements HAPEntityInfo{

	//name, for display
	protected String m_name;

	//description
	protected String m_description;
	
	protected HAPInfo m_info;

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
	public void cloneToEntityInfo(HAPEntityInfoWritable entityInfo) {
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
		this.buildEntityInfoByJson(json);
		return true;  
	}

	public void buildEntityInfoByJson(Object json) {
		JSONObject jsonObj = (JSONObject)json;
		this.m_name = jsonObj.optString(NAME);
		this.m_description = jsonObj.optString(DESCRIPTION);
		this.m_info = new HAPInfoImpSimple();
		this.m_info.buildObject(jsonObj.optJSONObject(INFO), HAPSerializationFormat.JSON);
	}
}
