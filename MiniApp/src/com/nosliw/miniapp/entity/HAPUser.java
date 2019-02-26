package com.nosliw.miniapp.entity;

import java.util.Map;

import org.json.JSONObject;

import com.nosliw.common.constant.HAPAttribute;
import com.nosliw.common.constant.HAPEntityWithAttribute;
import com.nosliw.common.info.HAPEntityInfoWritableImp;

@HAPEntityWithAttribute
public class HAPUser extends HAPEntityInfoWritableImp{

	@HAPAttribute
	public static String ID = "id";

	private String m_id;

	public HAPUser() {
	}
	
	public String getId() {  return this.m_id;  }
	public void setId(String id) {  this.m_id = id;   }

	
	@Override
	protected void buildJsonMap(Map<String, String> jsonMap, Map<String, Class<?>> typeJsonMap){
		super.buildJsonMap(jsonMap, typeJsonMap);
		jsonMap.put(ID, this.m_id);
	}
	
	@Override
	protected boolean buildObjectByFullJson(Object json){
		JSONObject jsonObj = (JSONObject)json;
		super.buildEntityInfoByJson(jsonObj);
		this.m_id = jsonObj.optString(ID);
		return true;
	}

	@Override
	protected boolean buildObjectByJson(Object json){	return this.buildObjectByFullJson(json);	}	
}
