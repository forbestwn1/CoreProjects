package com.nosliw.service.soccer;

import java.util.Map;

import org.json.JSONObject;

import com.nosliw.common.constant.HAPAttribute;
import com.nosliw.common.constant.HAPEntityWithAttribute;

@HAPEntityWithAttribute
public class HAPPlayerInfo extends HAPPlayerInfoCore{

	@HAPAttribute
	public static final String EMAIL = "email";
	
	private String m_email;

	public HAPPlayerInfo() {}
	
	public HAPPlayerInfo(String userId, String name, String email) {
		super(userId, name);
		this.m_email = email;
	}

	public String getEmail() {    return this.m_email;   }
	
	@Override
	protected void buildJsonMap(Map<String, String> jsonMap, Map<String, Class<?>> typeJsonMap){
		super.buildJsonMap(jsonMap, typeJsonMap);
		jsonMap.put(EMAIL, this.m_email);
	}
	
	@Override
	protected boolean buildObjectByJson(Object json){
		JSONObject jsonObj = (JSONObject)json;
		super.buildObjectByJson(jsonObj);
		this.m_email = (String)jsonObj.opt(EMAIL);
		return true;  
	}
}
