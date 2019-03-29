package com.nosliw.data.core.process.util;

import org.json.JSONArray;
import org.json.JSONObject;

import com.nosliw.common.serialization.HAPSerializationFormat;
import com.nosliw.data.core.process.HAPDefinitionActivity;
import com.nosliw.data.core.process.HAPDefinitionProcess;
import com.nosliw.data.core.process.HAPDefinitionProcessSuite;
import com.nosliw.data.core.process.plugin.HAPManagerActivityPlugin;
import com.nosliw.data.core.script.context.HAPContextGroup;
import com.nosliw.data.core.script.context.HAPParserContext;
import com.nosliw.data.core.script.context.dataassociation.HAPDefinitionWrapperTask;

public class HAPParserProcessDefinition {

	public static HAPDefinitionProcessSuite parsePocessSuite(JSONObject processSuiteJson, HAPManagerActivityPlugin activityPluginMan) {
		HAPDefinitionProcessSuite out = new HAPDefinitionProcessSuite();
		out.buildObject(processSuiteJson, HAPSerializationFormat.JSON);
		JSONArray processesArray = processSuiteJson.getJSONArray(HAPDefinitionProcessSuite.PROCESS);
		for(int i=0; i<processesArray.length(); i++){
			JSONObject processObjJson = processesArray.getJSONObject(i);
			HAPDefinitionProcess process = parseProcess(processObjJson, activityPluginMan);
			out.addProcess(processObjJson.getString("id"), process);
		}

		JSONObject contextJson = processSuiteJson.optJSONObject(HAPDefinitionProcessSuite.CONTEXT);
		HAPContextGroup context = HAPParserContext.parseContextGroup(contextJson);
		out.setContext(context);
		
		return out;
	}

	public static HAPDefinitionWrapperTask<HAPDefinitionProcess> parseEmbededProcess(JSONObject processJson, HAPManagerActivityPlugin activityPluginMan) {
		HAPDefinitionWrapperTask<HAPDefinitionProcess> out = new HAPDefinitionWrapperTask<HAPDefinitionProcess>();
		HAPDefinitionProcess process = new HAPDefinitionProcess();
		parseProcess(process, processJson, activityPluginMan);
		out.setTaskDefinition(process);
		out.buildMapping(processJson);
		return out;
	}
	
	public static HAPDefinitionProcess parseProcess(JSONObject processJson, HAPManagerActivityPlugin activityPluginMan) {
		HAPDefinitionProcess out = new HAPDefinitionProcess();
		parseProcess(out, processJson, activityPluginMan);
		return out;
	}

	public static void parseProcess(HAPDefinitionProcess out, JSONObject processJson, HAPManagerActivityPlugin activityPluginMan) {
		out.buildObject(processJson, HAPSerializationFormat.JSON);
		
		out.setContext(HAPParserContext.parseContextGroup(processJson.optJSONObject(HAPDefinitionProcess.CONTEXT)));
		
		JSONArray activityArrayJson = processJson.optJSONArray(HAPDefinitionProcess.ACTIVITY);
		for(int i=0; i<activityArrayJson.length(); i++) {
			JSONObject activityObjJson = (JSONObject)activityArrayJson.get(i);
			String activityType = activityObjJson.getString(HAPDefinitionActivity.TYPE);
			HAPDefinitionActivity activity = activityPluginMan.getPlugin(activityType).buildActivityDefinition(activityObjJson);
			out.addActivity(activityObjJson.getString("id"), activity);
		}
	}
}
