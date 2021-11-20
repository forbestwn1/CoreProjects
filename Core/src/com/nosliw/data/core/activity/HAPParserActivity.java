package com.nosliw.data.core.activity;

import org.json.JSONObject;

import com.nosliw.data.core.complex.HAPDefinitionEntityComplex;

public class HAPParserActivity {

	public static HAPDefinitionActivity parseActivityDefinition(JSONObject activityObjJson, HAPDefinitionEntityComplex complexEntity, HAPManagerActivityPlugin activityPluginMan) {
		String activityType = activityObjJson.getString(HAPDefinitionActivity.ACTIVITYTYPE);
		return activityPluginMan.getPlugin(activityType).buildActivityDefinition(activityObjJson, complexEntity);
	}
	
}
