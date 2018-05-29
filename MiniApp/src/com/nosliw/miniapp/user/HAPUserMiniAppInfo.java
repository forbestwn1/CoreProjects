package com.nosliw.miniapp.user;

import java.util.Map;

import org.json.JSONObject;

import com.nosliw.common.constant.HAPAttribute;
import com.nosliw.common.constant.HAPEntityWithAttribute;
import com.nosliw.common.serialization.HAPSerializableImp;

@HAPEntityWithAttribute
public class HAPUserMiniAppInfo extends HAPSerializableImp{

	@HAPAttribute
	public static String ID = "id";
	
	@HAPAttribute
	public static String APPID = "appId";
	
	@HAPAttribute
	public static String APPNAME = "appName";
	
	@HAPAttribute
	public static String GROUPID = "groupId";
	
	private String m_id;

	private String m_appId;

	private String m_appName;
	
	private String m_groupId;
	
	public HAPUserMiniAppInfo(String appId, String appName) {
		this.m_appId = appId;
		this.m_appName = appName;
	}
	
	@Override
	protected void buildJsonMap(Map<String, String> jsonMap, Map<String, Class<?>> typeJsonMap){
		jsonMap.put(ID, this.m_id);
		jsonMap.put(APPID, this.m_appId);
		jsonMap.put(APPNAME, this.m_appName);
		jsonMap.put(GROUPID, this.m_groupId);
	}
	
	@Override
	protected boolean buildObjectByFullJson(Object json){
		JSONObject jsonObj = (JSONObject)json;
		this.m_id = (String)jsonObj.opt(ID);
		this.m_appId = (String)jsonObj.opt(APPID);
		this.m_appName = (String)jsonObj.opt(APPNAME);
		this.m_groupId = (String)jsonObj.opt(GROUPID);
		return true;
	}

	@Override
	protected boolean buildObjectByJson(Object json){		return this.buildObjectByFullJson(json);	}	

}
