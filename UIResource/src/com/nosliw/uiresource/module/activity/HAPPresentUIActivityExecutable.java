package com.nosliw.uiresource.module.activity;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.json.JSONObject;

import com.nosliw.common.constant.HAPAttribute;
import com.nosliw.common.serialization.HAPJsonUtility;
import com.nosliw.common.serialization.HAPSerializationFormat;
import com.nosliw.data.core.activity.resource.HAPResourceIdActivityPlugin;
import com.nosliw.data.core.process1.HAPActivityPluginId;
import com.nosliw.data.core.process1.HAPExecutableActivityNormal;
import com.nosliw.data.core.resource.HAPResourceDependency;
import com.nosliw.data.core.resource.HAPResourceManagerRoot;
import com.nosliw.data.core.runtime.HAPRuntimeInfo;

public class HAPPresentUIActivityExecutable extends HAPExecutableActivityNormal{

	@HAPAttribute
	public static String UI = "ui";

	@HAPAttribute
	public static String SETTING = "setting";

	private String m_ui;

	private JSONObject m_setting;
	
	public HAPPresentUIActivityExecutable(String id, HAPPresentUIActivityDefinition activityDef) {
		super(id, activityDef);
		this.m_ui = activityDef.getUI();
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
		jsonMap.put(SETTING, HAPJsonUtility.buildJson(this.m_setting, HAPSerializationFormat.JSON));
	}

	@Override
	public List<HAPResourceDependency> getResourceDependency(HAPRuntimeInfo runtimeInfo, HAPResourceManagerRoot resourceManager) {
		List<HAPResourceDependency> out = new ArrayList<HAPResourceDependency>();
		out.add(new HAPResourceDependency(new HAPResourceIdActivityPlugin(new HAPActivityPluginId("UI_presentUI"))));
		return out;
	}
}
