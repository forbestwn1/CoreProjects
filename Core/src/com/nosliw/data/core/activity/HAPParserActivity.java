package com.nosliw.data.core.activity;

import org.json.JSONArray;
import org.json.JSONObject;

import com.nosliw.data.core.component.HAPDefinitionEntityComplex;

public class HAPParserActivity {

	public static HAPDefinitionActivitySuite parseActivitySuiteDefinition(JSONObject activitySuiteObjJson, HAPDefinitionEntityComplex complexEntity, HAPManagerActivityPlugin activityPluginMan) {
		HAPDefinitionActivitySuiteImp out = new HAPDefinitionActivitySuiteImp();
		out.buildEntityInfoByJson(activitySuiteObjJson);
		
		JSONArray activityArrayJson = activitySuiteObjJson.getJSONArray(HAPDefinitionActivitySuite.ELEMENT);
		for(int i=0; i<activityArrayJson.length(); i++) {
			JSONObject activityObjJson = (JSONObject)activityArrayJson.get(i);
			HAPDefinitionActivity activity = HAPParserActivity.parseActivityDefinition(activityObjJson, complexEntity, activityPluginMan);
			out.addEntityElement(activity);
		}
		return out;
	}
	
	public static HAPDefinitionActivity parseActivityDefinition(JSONObject activityObjJson, HAPDefinitionEntityComplex complexEntity, HAPManagerActivityPlugin activityPluginMan) {
		String activityType = activityObjJson.getString(HAPDefinitionActivity.TYPE);
		return activityPluginMan.getPlugin(activityType).buildActivityDefinition(activityObjJson, complexEntity);
	}
	
}
