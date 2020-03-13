package com.nosliw.data.core.expression;

import org.json.JSONArray;
import org.json.JSONObject;

import com.nosliw.data.core.component.HAPResourceDefinitionContainer;
import com.nosliw.data.core.component.HAPUtilityComponentParse;
import com.nosliw.data.core.process.HAPDefinitionProcessSuiteElementReference;

public class HAPParserExpressionSuite {

	public static HAPDefinitionExpressionSuite parseExpressionSuite(JSONObject expressionSuiteJson) {
		HAPDefinitionExpressionSuite out = new HAPDefinitionExpressionSuite();

		HAPUtilityComponentParse.parseComplextResourceDefinition(out, expressionSuiteJson);
		
		JSONArray processesArray = expressionSuiteJson.getJSONArray(HAPResourceDefinitionContainer.ELEMENT);
		for(int i=0; i<processesArray.length(); i++){
			JSONObject processObjJson = processesArray.getJSONObject(i);
			String id = processObjJson.getString("id");
			Object refObj = processObjJson.opt(HAPDefinitionProcessSuiteElementReference.REFERENCE);
			if(refObj==null) {
				//process
				out.addProcess(id, parseProcess(processObjJson, activityPluginMan));
			}
			else {
				//reference
				out.addProcess(id, parseProcessReference(processObjJson));
			}
		}
		return out;
	}

	
	
}
