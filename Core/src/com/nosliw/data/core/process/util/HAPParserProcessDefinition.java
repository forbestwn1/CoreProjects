package com.nosliw.data.core.process.util;

import org.json.JSONArray;
import org.json.JSONObject;

import com.nosliw.common.serialization.HAPSerializationFormat;
import com.nosliw.data.core.component.HAPUtilityComponentParse;
import com.nosliw.data.core.process.HAPDefinitionActivity;
import com.nosliw.data.core.process.HAPDefinitionProcessSuite;
import com.nosliw.data.core.process.HAPDefinitionProcessSuiteElementEntity;
import com.nosliw.data.core.process.HAPDefinitionProcessSuiteElementReference;
import com.nosliw.data.core.process.plugin.HAPManagerActivityPlugin;
import com.nosliw.data.core.script.context.dataassociation.HAPDefinitionWrapperTask;

public class HAPParserProcessDefinition {

	public static HAPDefinitionProcessSuite parsePocessSuite(JSONObject processSuiteJson, HAPManagerActivityPlugin activityPluginMan) {
		HAPDefinitionProcessSuite out = new HAPDefinitionProcessSuite();

		HAPUtilityComponentParse.parseComplextResourceDefinition(out, processSuiteJson);
		
		JSONArray processesArray = processSuiteJson.getJSONArray(HAPDefinitionProcessSuite.ELEMENT);
		for(int i=0; i<processesArray.length(); i++){
			JSONObject processObjJson = processesArray.getJSONObject(i);
			Object refObj = processObjJson.opt(HAPDefinitionProcessSuiteElementReference.REFERENCE);
			if(refObj==null) {
				//process
				out.addEntityElement(parseProcess(processObjJson, activityPluginMan));
			}
			else {
				//reference
				out.addEntityElement(parseProcessReference(processObjJson));
			}
		}
		return out;
	}

	public static HAPDefinitionWrapperTask<HAPDefinitionProcessSuiteElementEntity> parseEmbededProcess(JSONObject processJson, HAPManagerActivityPlugin activityPluginMan) {
		HAPDefinitionWrapperTask<HAPDefinitionProcessSuiteElementEntity> out = new HAPDefinitionWrapperTask<HAPDefinitionProcessSuiteElementEntity>();
		HAPDefinitionProcessSuiteElementEntity process = new HAPDefinitionProcessSuiteElementEntity();
		parseProcess(process, processJson, activityPluginMan);
		out.setTaskDefinition(process);
		out.buildMapping(processJson);
		return out;
	}
	
	private static HAPDefinitionProcessSuiteElementReference parseProcessReference(JSONObject processJson) {
		HAPDefinitionProcessSuiteElementReference out = new HAPDefinitionProcessSuiteElementReference();
		out.buildObject(processJson, HAPSerializationFormat.JSON);
		return out;
	}
	
	public static HAPDefinitionProcessSuiteElementEntity parseProcess(JSONObject processJson, HAPManagerActivityPlugin activityPluginMan) {
		HAPDefinitionProcessSuiteElementEntity out = new HAPDefinitionProcessSuiteElementEntity();
		parseProcess(out, processJson, activityPluginMan);
		return out;
	}

	public static void parseProcess(HAPDefinitionProcessSuiteElementEntity out, JSONObject processJson, HAPManagerActivityPlugin activityPluginMan) {
		HAPUtilityComponentParse.parseComponent(out, processJson);
		
		JSONArray activityArrayJson = processJson.optJSONArray(HAPDefinitionProcessSuiteElementEntity.ACTIVITY);
		for(int i=0; i<activityArrayJson.length(); i++) {
			JSONObject activityObjJson = (JSONObject)activityArrayJson.get(i);
			String activityType = activityObjJson.getString(HAPDefinitionActivity.TYPE);
			HAPDefinitionActivity activity = activityPluginMan.getPlugin(activityType).buildActivityDefinition(activityObjJson);
			out.addActivity(activityObjJson.getString("id"), activity);
		}
	}
}
