package com.nosliw.data.core.expression;

import org.json.JSONArray;
import org.json.JSONObject;

import com.nosliw.common.serialization.HAPSerializationFormat;
import com.nosliw.data.core.component.HAPResourceDefinitionContainer;
import com.nosliw.data.core.component.HAPUtilityComponentParse;

public class HAPParserExpressionDefinition {

	public static HAPResourceDefinitionExpressionSuite parseExpressionSuite(JSONObject expressionSuiteJson, HAPParserExpression expressionParser) {
		HAPResourceDefinitionExpressionSuite out = new HAPResourceDefinitionExpressionSuite();

		HAPUtilityComponentParse.parseComplextResourceDefinition(out, expressionSuiteJson);
		
		JSONArray processesArray = expressionSuiteJson.getJSONArray(HAPResourceDefinitionContainer.ELEMENT);
		for(int i=0; i<processesArray.length(); i++){
			JSONObject processObjJson = processesArray.getJSONObject(i);
			String id = processObjJson.getString("id");
			Object expressionObj = processObjJson.opt(HAPDefinitionExpressionSuiteElementEntity.ELEMENT);
			if(expressionObj!=null) {
				//process
				out.addElement(id, parseExpressionSuiteElement(processObjJson, expressionParser));
			}
			else {
				//reference
//				out.addProcess(id, parseProcessReference(processObjJson));
			}
		}
		return out;
	}

	public static HAPDefinitionExpressionSuiteElementEntity parseExpressionSuiteElement(JSONObject jsonObj, HAPParserExpression expressionParser) {
		HAPDefinitionExpressionSuiteElementEntity out = new HAPDefinitionExpressionSuiteElementEntity();
		HAPUtilityComponentParse.parseComponent(out, jsonObj);

		JSONArray eleArrayJson = jsonObj.optJSONArray(HAPDefinitionExpressionSuiteElementEntity.ELEMENT);
		if(eleArrayJson!=null) {
			for(int i=0; i<eleArrayJson.length(); i++) {
				JSONObject expressionJsonObj = eleArrayJson.getJSONObject(i);
				Object expressionObj = expressionJsonObj.opt(HAPDefinitionExpressionSuiteElementEntityExpression.EXPRESSION);
				if(expressionObj!=null) {
					//process
					out.addExpression(parseExpressionDefinition(expressionJsonObj, expressionParser));
				}
				else {
					//reference
//					out.addProcess(id, parseProcessReference(processObjJson));
				}
			}
		}
		else {
			Object expressionObj = jsonObj.opt(HAPDefinitionExpressionSuiteElementEntityExpression.EXPRESSION);
			if(expressionObj!=null) {
				//process
				out.addExpression(parseExpressionDefinition(jsonObj, expressionParser));
			}
			else {
				//reference
//				out.addProcess(id, parseProcessReference(processObjJson));
			}
		}
		return out;
	}

	private static HAPDefinitionExpressionSuiteElementEntityExpression parseExpressionDefinition(JSONObject jsonObj, HAPParserExpression expressionParser) {
		HAPDefinitionExpressionSuiteElementEntityExpression out = new HAPDefinitionExpressionSuiteElementEntityExpression();
		out.buildEntityInfoByJson(jsonObj);
		String expressionStr = jsonObj.getString(HAPDefinitionExpressionSuiteElementEntityExpression.EXPRESSION);
		out.setOperand(expressionParser.parseExpression(expressionStr));
		
		JSONArray refJsonArray = jsonObj.optJSONArray(HAPDefinitionExpressionSuiteElementEntityExpression.REFERENCEMAPPING);
		if(refJsonArray!=null) {
			for(int i=0; i<refJsonArray.length(); i++) {
				JSONObject refJsonObj = refJsonArray.getJSONObject(i);
				HAPDefinitionReference ref = new HAPDefinitionReference();
				ref.buildObject(refJsonObj, HAPSerializationFormat.JSON);
				out.addReference(ref);
			}
		}
		return out;
	}
}
