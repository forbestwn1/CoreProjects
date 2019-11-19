package com.nosliw.service.soccer;

import java.util.Map;

import org.json.JSONObject;

import com.nosliw.common.constant.HAPAttribute;
import com.nosliw.data.core.runtime.HAPExecutableImp;

public class HAPPlayerInfoCore extends HAPExecutableImp{

	@HAPAttribute
	public static final String USERID = "userId";
	
	@HAPAttribute
	public static final String NAME = "name";
	
	protected String m_userId;
	
	protected String m_name;
	
	public HAPPlayerInfoCore() {}
	
	public HAPPlayerInfoCore(String userId, String name) {
		this.m_userId = userId;
		this.m_name = name;
	}

	public String getUserId() {   return this.m_userId;    }
	public String getName() {  return this.m_name;   }

	@Override
	protected void buildJsonMap(Map<String, String> jsonMap, Map<String, Class<?>> typeJsonMap){
		jsonMap.put(NAME, this.m_name);
		jsonMap.put(USERID, this.m_userId);
	}
	
	@Override
	protected boolean buildObjectByJson(Object json){
		JSONObject jsonObj = (JSONObject)json;
		super.buildObjectByJson(jsonObj);
		this.m_name = (String)jsonObj.opt(NAME);
		this.m_userId = (String)jsonObj.opt(USERID);
		return true;  
	}
	
}
