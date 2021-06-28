package com.nosliw.data.core.activity;

import org.json.JSONArray;
import org.json.JSONObject;

public class HAPParserActivity {

	public static HAPDefinitionActivitySuite parseActivitySuiteDefinition(JSONObject activitySuiteObjJson, HAPManagerActivityPlugin activityPluginMan) {
		HAPDefinitionActivitySuiteImp out = new HAPDefinitionActivitySuiteImp();
		out.buildEntityInfoByJson(activitySuiteObjJson);
		
		JSONArray activityArrayJson = activitySuiteObjJson.getJSONArray(HAPDefinitionActivitySuite.ELEMENT);
		for(int i=0; i<activityArrayJson.length(); i++) {
			JSONObject activityObjJson = (JSONObject)activityArrayJson.get(i);
			HAPDefinitionActivity activity = HAPParserActivity.parseActivityDefinition(activityObjJson, activityPluginMan);
			out.addEntityElement(activity);
		}
		return out;
	}
	
	public static HAPDefinitionActivity parseActivityDefinition(JSONObject activityObjJson, HAPManagerActivityPlugin activityPluginMan) {
		String activityType = activityObjJson.getString(HAPDefinitionActivity.TYPE);
		return activityPluginMan.getPlugin(activityType).buildActivityDefinition(activityObjJson);
	}
	
}
