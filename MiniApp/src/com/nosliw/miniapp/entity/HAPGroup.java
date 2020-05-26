package com.nosliw.miniapp.entity;

import java.util.Map;

import org.json.JSONObject;

import com.nosliw.common.constant.HAPEntityWithAttribute;
import com.nosliw.common.info.HAPEntityInfoWritableImp;

@HAPEntityWithAttribute
public class HAPGroup  extends HAPEntityInfoWritableImp{

	public HAPGroup() {}

	public HAPGroup(String id, String name) {
		this.setId(id);
		this.setName(name);
	}

	@Override
	protected void buildJsonMap(Map<String, String> jsonMap, Map<String, Class<?>> typeJsonMap){
		super.buildJsonMap(jsonMap, typeJsonMap);
	}
	
	@Override
	protected boolean buildObjectByFullJson(Object json){
		JSONObject jsonObj = (JSONObject)json;
		super.buildEntityInfoByJson(jsonObj);
		this.setName(jsonObj.optString(NAME));
		return true;
	}

	@Override
	protected boolean buildObjectByJson(Object json){	return this.buildObjectByFullJson(json);	}	
}
