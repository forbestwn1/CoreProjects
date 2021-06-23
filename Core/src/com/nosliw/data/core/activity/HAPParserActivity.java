package com.nosliw.data.core.activity;

import org.json.JSONObject;

public class HAPParserActivity {

	public static HAPDefinitionActivity parseActivityDefinition(JSONObject activityObjJson, HAPManagerActivityPlugin activityPluginMan) {
		String activityType = activityObjJson.getString(HAPDefinitionActivity.TYPE);
		return activityPluginMan.getPlugin(activityType).buildActivityDefinition(activityObjJson);
	}
	
}
