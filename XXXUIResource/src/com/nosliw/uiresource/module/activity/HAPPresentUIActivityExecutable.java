package com.nosliw.uiresource.module.activity;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.json.JSONObject;

import com.nosliw.common.constant.HAPAttribute;
import com.nosliw.common.serialization.HAPUtilityJson;
import com.nosliw.core.resource.HAPManagerResource;
import com.nosliw.core.resource.HAPResourceDependency;
import com.nosliw.common.serialization.HAPSerializationFormat;
import com.nosliw.data.core.activity.HAPExecutableActivity;
import com.nosliw.data.core.activity.HAPPluginResourceIdActivity;
import com.nosliw.data.core.process1.HAPActivityPluginId;
import com.nosliw.data.core.runtime.HAPRuntimeInfo;

public class HAPPresentUIActivityExecutable extends HAPExecutableActivity{

	@HAPAttribute
	public static String UI = "ui";

	@HAPAttribute
	public static String SETTING = "setting";

	private String m_ui;

	private JSONObject m_setting;
	
	public HAPPresentUIActivityExecutable(String id, HAPPresentUIActivityDefinition activityDef) {
		super(activityDef.getActivityType(), id, activityDef);
		this.m_ui = activityDef.getPage();
		this.m_setting = activityDef.getSetting();
	}
	
	@Override
	protected boolean buildObjectByJson(Object json){
		super.buildObjectByJson(json);
		JSONObject jsonObj = (JSONObject)json;
		this.m_ui = jsonObj.getString(UI);
		this.m_setting = jsonObj.getJSONObject(SETTING);
		return true;  
	}
	
	@Override
	protected void buildJsonMap(Map<String, String> jsonMap, Map<String, Class<?>> typeJsonMap) {
		super.buildJsonMap(jsonMap, typeJsonMap);
		jsonMap.put(UI, this.m_ui);
		jsonMap.put(SETTING, HAPUtilityJson.buildJson(this.m_setting, HAPSerializationFormat.JSON));
	}

	@Override
	public List<HAPResourceDependency> getResourceDependency(HAPRuntimeInfo runtimeInfo, HAPManagerResource resourceManager) {
		List<HAPResourceDependency> out = new ArrayList<HAPResourceDependency>();
		out.add(new HAPResourceDependency(new HAPPluginResourceIdActivity(new HAPActivityPluginId("UI_presentUI"))));
		return out;
	}
}
