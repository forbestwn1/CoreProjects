package com.nosliw.service.soccer;

import java.util.Map;

import org.json.JSONObject;

import com.nosliw.common.constant.HAPAttribute;
import com.nosliw.common.constant.HAPEntityWithAttribute;
import com.nosliw.data.core.runtime.HAPExecutableImp;

@HAPEntityWithAttribute
public class HAPPlayerInfo extends HAPExecutableImp{

	@HAPAttribute
	public static final String NAME = "name";
	
	@HAPAttribute
	public static final String EMAIL = "email";
	
	private String m_name;
	
	private String m_email;

	public HAPPlayerInfo() {}
	
	public HAPPlayerInfo(String name, String email) {
		this.m_name = name;
		this.m_email = email;
	}

	public String getName() {  return this.m_name;   }
	public String getEmail() {    return this.m_email;   }
	
	@Override
	protected void buildJsonMap(Map<String, String> jsonMap, Map<String, Class<?>> typeJsonMap){
		jsonMap.put(NAME, this.m_name);
		jsonMap.put(EMAIL, this.m_email);
	}
	
	@Override
	protected boolean buildObjectByJson(Object json){
		JSONObject jsonObj = (JSONObject)json;
		super.buildObjectByJson(jsonObj);
		this.m_name = (String)jsonObj.opt(NAME);
		this.m_email = (String)jsonObj.opt(EMAIL);
		return true;  
	}
}
