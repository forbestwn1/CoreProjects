package com.nosliw.uiresource.module.activity;

import java.util.Map;

import org.json.JSONObject;

import com.nosliw.common.constant.HAPAttribute;
import com.nosliw.data.core.process.HAPDefinitionActivityNormal;

public class HAPPresentUIActivityDefinition extends HAPDefinitionActivityNormal{

	@HAPAttribute
	public static String UI = "ui";
	
	private String m_ui;

	public HAPPresentUIActivityDefinition(String type) {
		super(type);
	}
	
	public String getUI() {  return this.m_ui;   }
	public void setUI(String ui) {   this.m_ui = ui;    }

	@Override
	protected boolean buildObjectByJson(Object json){
		super.buildObjectByJson(json);
		JSONObject jsonObj = (JSONObject)json;
		this.setUI(jsonObj.optString(UI));
		return true;  
	}

	@Override
	protected void buildJsonMap(Map<String, String> jsonMap, Map<String, Class<?>> typeJsonMap) {
		super.buildJsonMap(jsonMap, typeJsonMap);
		jsonMap.put(UI, this.m_ui);
	}

}
