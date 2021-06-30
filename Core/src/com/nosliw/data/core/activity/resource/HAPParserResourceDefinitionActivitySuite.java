package com.nosliw.data.core.activity.resource;

import org.json.JSONArray;
import org.json.JSONObject;

import com.nosliw.data.core.activity.HAPDefinitionActivity;
import com.nosliw.data.core.activity.HAPManagerActivityPlugin;
import com.nosliw.data.core.activity.HAPParserActivity;
import com.nosliw.data.core.common.HAPWithEntityElement;
import com.nosliw.data.core.component.HAPUtilityComponentParse;
import com.nosliw.data.core.resource.HAPParserResourceDefinitionImp;
import com.nosliw.data.core.resource.HAPResourceDefinition;

public class HAPParserResourceDefinitionActivitySuite extends HAPParserResourceDefinitionImp{

	private HAPManagerActivityPlugin m_activityPluginMan;
	
	public HAPParserResourceDefinitionActivitySuite(HAPManagerActivityPlugin activityPluginMan) {
		this.m_activityPluginMan = activityPluginMan;
	}
	
	@Override
	public HAPResourceDefinition parseJson(JSONObject jsonObj) {
		HAPResourceDefinitionActivitySuite out = new HAPResourceDefinitionActivitySuite();

		HAPUtilityComponentParse.parseComplextResourceDefinition(out, jsonObj);
		
		JSONArray activityArrayJson = jsonObj.getJSONArray(HAPWithEntityElement.ELEMENT);
		for(int i=0; i<activityArrayJson.length(); i++) {
			JSONObject activityObjJson = (JSONObject)activityArrayJson.get(i);
			HAPDefinitionActivity activity = HAPParserActivity.parseActivityDefinition(activityObjJson, out, m_activityPluginMan);
			out.addEntityElement(activity);
		}
		return out;
	}
}
