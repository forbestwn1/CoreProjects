package com.nosliw.miniapp.user;

import java.util.Map;

import org.json.JSONObject;

import com.nosliw.common.constant.HAPAttribute;
import com.nosliw.common.constant.HAPEntityWithAttribute;
import com.nosliw.common.info.HAPEntityInfoWritableImp;

@HAPEntityWithAttribute
public class HAPMiniApp extends HAPEntityInfoWritableImp{

	@HAPAttribute
	public static String APPID = "appId";
	
	@HAPAttribute
	public static String GROUPID = "groupId";
	
	private String m_appId;

	private String m_groupId;
	
	
	@Override
	protected void buildJsonMap(Map<String, String> jsonMap, Map<String, Class<?>> typeJsonMap){
		super.buildJsonMap(jsonMap, typeJsonMap);
		jsonMap.put(APPID, this.m_appId);
		jsonMap.put(GROUPID, this.m_groupId);
	}
	
	@Override
	protected boolean buildObjectByJson(Object json){
		JSONObject jsonObj = (JSONObject)json;
		this.buildEntityInfoByJson(jsonObj);
		this.m_appId = (String)jsonObj.opt(APPID);
		this.m_groupId = (String)jsonObj.opt(GROUPID);
		return true;
	}
}
