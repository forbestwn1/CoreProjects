package com.nosliw.data.core.expression.resource;

import org.json.JSONArray;
import org.json.JSONObject;

import com.nosliw.common.serialization.HAPSerializationFormat;
import com.nosliw.data.core.component.HAPResourceDefinitionContainer;
import com.nosliw.data.core.component.HAPUtilityComponentParse;
import com.nosliw.data.core.expression.HAPDefinitionExpression;

public class HAPParserResourceExpressionDefinition {

	public static HAPResourceDefinitionExpressionSuite parseExpressionSuite(JSONObject expressionSuiteJson) {
		HAPResourceDefinitionExpressionSuite out = new HAPResourceDefinitionExpressionSuite();

		HAPUtilityComponentParse.parseComplextResourceDefinition(out, expressionSuiteJson);
		
		JSONArray processesArray = expressionSuiteJson.getJSONArray(HAPResourceDefinitionContainer.ELEMENT);
		for(int i=0; i<processesArray.length(); i++){
			JSONObject processObjJson = processesArray.getJSONObject(i);
			Object expressionObj = processObjJson.opt(HAPDefinitionResourceDefinitionExpressionSuiteElementEntity.ELEMENT);
			if(expressionObj!=null) {
				//process
				out.addContainerElement(parseExpressionSuiteElement(processObjJson));
			}
			else {
				//reference
//				out.addProcess(id, parseProcessReference(processObjJson));
			}
		}
		return out;
	}

	public static HAPDefinitionResourceDefinitionExpressionSuiteElementEntity parseExpressionSuiteElement(JSONObject jsonObj) {
		HAPDefinitionResourceDefinitionExpressionSuiteElementEntity out = new HAPDefinitionResourceDefinitionExpressionSuiteElementEntity();
		HAPUtilityComponentParse.parseComponent(out, jsonObj);

		JSONArray eleArrayJson = jsonObj.optJSONArray(HAPDefinitionResourceDefinitionExpressionSuiteElementEntity.ELEMENT);
		if(eleArrayJson!=null) {
			for(int i=0; i<eleArrayJson.length(); i++) {
				JSONObject expressionJsonObj = eleArrayJson.getJSONObject(i);
				Object expressionObj = expressionJsonObj.opt(HAPDefinitionExpression.EXPRESSION);
				if(expressionObj!=null) {
					//process
					out.addEntityElement(parseExpressionDefinition(expressionJsonObj));
				}
				else {
					//reference
//					out.addProcess(id, parseProcessReference(processObjJson));
				}
			}
		}
		else {
			Object expressionObj = jsonObj.opt(HAPDefinitionExpression.EXPRESSION);
			if(expressionObj!=null) {
				//process
				out.addEntityElement(parseExpressionDefinition(jsonObj));
			}
			else {
				//reference
//				out.addProcess(id, parseProcessReference(processObjJson));
			}
		}
		return out;
	}
	
	private static HAPDefinitionExpression parseExpressionDefinition(JSONObject jsonObj) {
		HAPDefinitionExpression out = new HAPDefinitionExpression();
		out.buildObject(jsonObj, HAPSerializationFormat.JSON);
		return out;
	}

}
