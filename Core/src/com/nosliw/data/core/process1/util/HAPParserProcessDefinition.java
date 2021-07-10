package com.nosliw.data.core.process1.util;

import org.json.JSONArray;
import org.json.JSONObject;

import com.nosliw.common.serialization.HAPSerializationFormat;
import com.nosliw.data.core.activity.HAPDefinitionActivity;
import com.nosliw.data.core.activity.HAPManagerActivityPlugin;
import com.nosliw.data.core.component.HAPUtilityComponentParse;
import com.nosliw.data.core.dataassociation.HAPDefinitionWrapperTask;
import com.nosliw.data.core.process1.resource.HAPElementContainerResourceDefinitionEntityImpComponentProcessSuite;
import com.nosliw.data.core.process1.resource.HAPElementContainerResourceDefinitionReferenceProcessSuite;
import com.nosliw.data.core.process1.resource.HAPResourceDefinitionProcessSuite;

public class HAPParserProcessDefinition {

	public static HAPResourceDefinitionProcessSuite parsePocessSuite(JSONObject processSuiteJson, HAPManagerActivityPlugin activityPluginMan) {
		HAPResourceDefinitionProcessSuite out = new HAPResourceDefinitionProcessSuite();

		HAPUtilityComponentParse.parseComplextResourceDefinition(out, processSuiteJson);
		
		JSONArray processesArray = processSuiteJson.getJSONArray(HAPResourceDefinitionProcessSuite.ELEMENT);
		for(int i=0; i<processesArray.length(); i++){
			JSONObject processObjJson = processesArray.getJSONObject(i);
			Object refObj = processObjJson.opt(HAPElementContainerResourceDefinitionReferenceProcessSuite.REFERENCE);
			if(refObj==null) {
				//process
				out.addContainerElement(parseProcess(processObjJson, activityPluginMan));
			}
			else {
				//reference
				out.addContainerElement(parseProcessReference(processObjJson));
			}
		}
		return out;
	}

	public static HAPDefinitionWrapperTask<HAPElementContainerResourceDefinitionEntityImpComponentProcessSuite> parseEmbededProcess(JSONObject processJson, HAPManagerActivityPlugin activityPluginMan) {
		HAPDefinitionWrapperTask<HAPElementContainerResourceDefinitionEntityImpComponentProcessSuite> out = new HAPDefinitionWrapperTask<HAPElementContainerResourceDefinitionEntityImpComponentProcessSuite>();
		HAPElementContainerResourceDefinitionEntityImpComponentProcessSuite process = new HAPElementContainerResourceDefinitionEntityImpComponentProcessSuite();
		parseProcess(process, processJson, activityPluginMan);
		out.buildObj(processJson, process);
		return out;
	}
	
	private static HAPElementContainerResourceDefinitionReferenceProcessSuite parseProcessReference(JSONObject processJson) {
		HAPElementContainerResourceDefinitionReferenceProcessSuite out = new HAPElementContainerResourceDefinitionReferenceProcessSuite();
		out.buildObject(processJson, HAPSerializationFormat.JSON);
		return out;
	}
	
	public static HAPElementContainerResourceDefinitionEntityImpComponentProcessSuite parseProcess(JSONObject processJson, HAPManagerActivityPlugin activityPluginMan) {
		HAPElementContainerResourceDefinitionEntityImpComponentProcessSuite out = new HAPElementContainerResourceDefinitionEntityImpComponentProcessSuite();
		parseProcess(out, processJson, activityPluginMan);
		return out;
	}

	public static void parseProcess(HAPElementContainerResourceDefinitionEntityImpComponentProcessSuite out, JSONObject processJson, HAPManagerActivityPlugin activityPluginMan) {
		HAPUtilityComponentParse.parseComponent(out, processJson);
		
		JSONArray activityArrayJson = processJson.optJSONArray(HAPElementContainerResourceDefinitionEntityImpComponentProcessSuite.ACTIVITY);
		for(int i=0; i<activityArrayJson.length(); i++) {
			JSONObject activityObjJson = (JSONObject)activityArrayJson.get(i);
			String activityType = activityObjJson.getString(HAPDefinitionActivity.TYPE);
			HAPDefinitionActivity activity = activityPluginMan.getPlugin(activityType).buildActivityDefinition(activityObjJson);
			out.addActivity(activityObjJson.getString("id"), activity);
		}
	}
}
