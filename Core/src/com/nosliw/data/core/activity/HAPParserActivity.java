package com.nosliw.data.core.activity;

import org.json.JSONObject;

import com.nosliw.core.application.division.manual.HAPManualEntityComplex;

public class HAPParserActivity {

	public static HAPDefinitionActivity parseActivityDefinition(JSONObject activityObjJson, HAPManualEntityComplex complexEntity, HAPManagerActivityPlugin activityPluginMan) {
		String activityType = activityObjJson.getString(HAPDefinitionActivity.ACTIVITYTYPE);
		return activityPluginMan.getPlugin(activityType).buildActivityDefinition(activityObjJson, complexEntity);
	}
	
}
