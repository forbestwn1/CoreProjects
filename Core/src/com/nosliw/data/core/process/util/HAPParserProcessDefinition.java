package com.nosliw.data.core.process.util;

import org.json.JSONArray;
import org.json.JSONObject;

import com.nosliw.common.serialization.HAPSerializationFormat;
import com.nosliw.data.core.process.HAPDefinitionActivity;
import com.nosliw.data.core.process.HAPDefinitionProcess;
import com.nosliw.data.core.process.HAPDefinitionProcessSuite;
import com.nosliw.data.core.process.HAPManagerProcess;
import com.nosliw.data.core.script.context.HAPContextGroup;
import com.nosliw.data.core.script.context.HAPParserContext;

public class HAPParserProcessDefinition {

	public static HAPDefinitionProcessSuite parsePocessSuite(JSONObject processSuiteJson, HAPManagerProcess processMan) {
		HAPDefinitionProcessSuite out = new HAPDefinitionProcessSuite();
		out.buildObject(processSuiteJson, HAPSerializationFormat.JSON);
		JSONArray processesArray = processSuiteJson.getJSONArray(HAPDefinitionProcessSuite.PROCESS);
		for(int i=0; i<processesArray.length(); i++){
			HAPDefinitionProcess process = parseProcess(processesArray.getJSONObject(i), processMan);
			out.addProcess(process);
		}

		JSONObject contextJson = processSuiteJson.optJSONObject(HAPDefinitionProcessSuite.CONTEXT);
		HAPContextGroup context = HAPParserContext.parseContextGroup(contextJson);
		out.setContext(context);
		
		return out;
	}

	
	public static HAPDefinitionProcess parseProcess(JSONObject processJson, HAPManagerProcess processMan) {
		HAPDefinitionProcess out = new HAPDefinitionProcess();
		out.buildObject(processJson, HAPSerializationFormat.JSON);
		
		out.setContext(HAPParserContext.parseContextGroup(processJson.optJSONObject(HAPDefinitionProcess.CONTEXT)));
		
		JSONArray activityArrayJson = processJson.optJSONArray(HAPDefinitionProcess.ACTIVITY);
		for(int i=0; i<activityArrayJson.length(); i++) {
			JSONObject activityObjJson = (JSONObject)activityArrayJson.get(i);
			String stepType = activityObjJson.getString(HAPDefinitionActivity.TYPE);
			HAPDefinitionActivity activity = processMan.getActivityPlugin(stepType).buildActivityDefinition(activityObjJson);
			out.addActivity(activity);
		}
		return out;
	}
}
