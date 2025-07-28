package com.nosliw.uiresource.application;

import java.util.Map;

import org.json.JSONObject;

import com.nosliw.common.constant.HAPEntityWithAttribute;
import com.nosliw.data.core.structure.story.HAPContextEntity;

@HAPEntityWithAttribute
public class HAPDefinitionAppData extends HAPContextEntity{

	public HAPDefinitionAppData() {
	}

	public HAPDefinitionAppData cloneAppDataDefinition() {
		HAPDefinitionAppData out = new HAPDefinitionAppData();
		this.toContextEntity(out);
		return out;
	}
	
	@Override
	public boolean buildObjectByJson(Object obj) {
		super.buildObjectByJson(obj);
		JSONObject jsonObj = (JSONObject)obj;
		return true;
	}
	
	@Override
	protected void buildJsonMap(Map<String, String> jsonMap, Map<String, Class<?>> typeJsonMap){
		super.buildJsonMap(jsonMap, typeJsonMap);
	}
}
