package com.nosliw.data.core.activity;

import org.json.JSONObject;

import com.nosliw.core.application.division.manual.HAPManualBrickComplex;

public class HAPParserActivity {

	public static HAPDefinitionActivity parseActivityDefinition(JSONObject activityObjJson, HAPManualBrickComplex complexEntity, HAPManagerActivityPlugin activityPluginMan) {
		String activityType = activityObjJson.getString(HAPDefinitionActivity.ACTIVITYTYPE);
		return activityPluginMan.getPlugin(activityType).buildActivityDefinition(activityObjJson, complexEntity);
	}
	
}
