package com.nosliw.miniapp.user;

import java.util.Map;

import org.json.JSONObject;

import com.nosliw.common.constant.HAPAttribute;
import com.nosliw.common.constant.HAPEntityWithAttribute;
import com.nosliw.common.info.HAPEntityInfoWritableImp;

@HAPEntityWithAttribute
public class HAPGroup  extends HAPEntityInfoWritableImp{

	@HAPAttribute
	public static String ID = "id";

	private String m_id;

	public HAPGroup() {}

	public HAPGroup(String id, String name) {
		this.m_id = id;
		this.m_name = name;
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
		this.m_name = jsonObj.optString(NAME);
		return true;
	}

	@Override
	protected boolean buildObjectByJson(Object json){	return this.buildObjectByFullJson(json);	}	
}
