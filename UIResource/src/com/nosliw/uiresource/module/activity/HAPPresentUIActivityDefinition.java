package com.nosliw.uiresource.module.activity;

import java.util.Map;

import org.json.JSONObject;

import com.nosliw.common.constant.HAPAttribute;
import com.nosliw.data.core.activity.HAPDefinitionActivity;
import com.nosliw.data.core.activity.HAPDefinitionActivityNormal;

public class HAPPresentUIActivityDefinition extends HAPDefinitionActivityNormal{

	@HAPAttribute
	public static String UI = "ui";
	
	@HAPAttribute
	public static String SETTING = "setting";
	
	private String m_ui;

	private JSONObject m_setting;
	
	public HAPPresentUIActivityDefinition(String type) {
		super(type);
	}
	
	public String getUI() {  return this.m_ui;   }
	public void setUI(String ui) {   this.m_ui = ui;    }
	
	public JSONObject getSetting() {   return this.m_setting;   }

	@Override
	protected boolean buildObjectByJson(Object json){
		super.buildObjectByJson(json);
		JSONObject jsonObj = (JSONObject)json;
		this.setUI(jsonObj.optString(UI));
		this.m_setting = jsonObj.optJSONObject(SETTING);
		return true;  
	}

	@Override
	protected void buildJsonMap(Map<String, String> jsonMap, Map<String, Class<?>> typeJsonMap) {
		super.buildJsonMap(jsonMap, typeJsonMap);
		jsonMap.put(UI, this.m_ui);
	}

	@Override
	public HAPDefinitionActivity cloneActivityDefinition() {
		HAPPresentUIActivityDefinition out = new HAPPresentUIActivityDefinition(this.getActivityType());
		this.cloneToNormalActivityDefinition(out);
		out.m_ui = this.m_ui;
		out.m_setting = this.m_setting;
		return out;
	}

}
