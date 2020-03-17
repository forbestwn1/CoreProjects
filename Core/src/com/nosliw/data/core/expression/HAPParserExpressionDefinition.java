package com.nosliw.data.core.expression;

import org.json.JSONArray;
import org.json.JSONObject;

import com.nosliw.data.core.component.HAPResourceDefinitionContainer;
import com.nosliw.data.core.component.HAPUtilityComponentParse;
import com.nosliw.data.core.process.HAPDefinitionProcessSuiteElementReference;

public class HAPParserExpressionDefinition {

	public static HAPDefinitionExpressionSuite parseExpressionSuite(JSONObject expressionSuiteJson, HAPParserExpression expressionParser) {
		HAPDefinitionExpressionSuite out = new HAPDefinitionExpressionSuite();

		HAPUtilityComponentParse.parseComplextResourceDefinition(out, expressionSuiteJson);
		
		JSONArray processesArray = expressionSuiteJson.getJSONArray(HAPResourceDefinitionContainer.ELEMENT);
		for(int i=0; i<processesArray.length(); i++){
			JSONObject processObjJson = processesArray.getJSONObject(i);
			String id = processObjJson.getString("id");
			Object refObj = processObjJson.opt(HAPDefinitionProcessSuiteElementReference.REFERENCE);
			if(refObj==null) {
				//process
				out.addElement(id, parseExpressionDefinition(processObjJson, expressionParser));
			}
			else {
				//reference
//				out.addProcess(id, parseProcessReference(processObjJson));
			}
		}
		return out;
	}

	private static HAPDefinitionExpressionSuiteElementEntity parseExpressionDefinition(JSONObject jsonObj, HAPParserExpression expressionParser) {
		HAPDefinitionExpressionSuiteElementEntity out = new HAPDefinitionExpressionSuiteElementEntity();
		HAPUtilityComponentParse.parseComponent(out, jsonObj);
		String expressionStr = jsonObj.getString(HAPDefinitionExpressionSuiteElementEntity.EXPRESSION);
		out.setOperand(expressionParser.parseExpression(expressionStr));
		return out;
	}
	
}
