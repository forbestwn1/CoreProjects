package com.nosliw.data.core.activity;

import org.json.JSONObject;

import com.nosliw.data.core.complex.HAPDefinitionEntityInDomainComplex;

public class HAPParserActivity {

	public static HAPDefinitionActivity parseActivityDefinition(JSONObject activityObjJson, HAPDefinitionEntityInDomainComplex complexEntity, HAPManagerActivityPlugin activityPluginMan) {
		String activityType = activityObjJson.getString(HAPDefinitionActivity.ACTIVITYTYPE);
		return activityPluginMan.getPlugin(activityType).buildActivityDefinition(activityObjJson, complexEntity);
	}
	
}
