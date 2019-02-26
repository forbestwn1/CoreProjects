package com.nosliw.miniapp.user;

import java.util.Map;

import org.json.JSONObject;

import com.nosliw.common.constant.HAPAttribute;
import com.nosliw.common.constant.HAPEntityWithAttribute;
import com.nosliw.common.info.HAPEntityInfoWritableImp;

@HAPEntityWithAttribute
public class HAPMiniApp extends HAPEntityInfoWritableImp{

	@HAPAttribute
	public static String ID = "id";
	
	private String m_id;

	public HAPMiniApp(String id, String name) {
		this.m_id = id;
		this.setName(name);
	}
	
	@Override
	protected void buildJsonMap(Map<String, String> jsonMap, Map<String, Class<?>> typeJsonMap){
		super.buildJsonMap(jsonMap, typeJsonMap);
		jsonMap.put(ID, this.m_id);
	}
	
	@Override
	protected boolean buildObjectByJson(Object json){
		JSONObject jsonObj = (JSONObject)json;
		this.buildEntityInfoByJson(jsonObj);
		this.m_id = (String)jsonObj.opt(ID);
		return true;
	}
}
