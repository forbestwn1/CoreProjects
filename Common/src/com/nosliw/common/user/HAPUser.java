package com.nosliw.common.user;

import java.util.Map;

import org.json.JSONObject;

import com.nosliw.common.constant.HAPEntityWithAttribute;
import com.nosliw.common.info.HAPEntityInfoWritableImp;

@HAPEntityWithAttribute
public class HAPUser extends HAPEntityInfoWritableImp{

	public HAPUser() {
	}
	
	@Override
	protected void buildJsonMap(Map<String, String> jsonMap, Map<String, Class<?>> typeJsonMap){
		super.buildJsonMap(jsonMap, typeJsonMap);
	}
	
	@Override
	protected boolean buildObjectByFullJson(Object json){
		JSONObject jsonObj = (JSONObject)json;
		super.buildEntityInfoByJson(jsonObj);
		return true;
	}

	@Override
	protected boolean buildObjectByJson(Object json){	return this.buildObjectByFullJson(json);	}	
}
