package com.nosliw.data.core.expression;

import org.json.JSONArray;
import org.json.JSONObject;

import com.nosliw.common.serialization.HAPSerializationFormat;

public class HAPParserExpressionDefinition {

	public static void parseExpressionDefinitionList(HAPDefinitionExpressionGroup expressionGroup, JSONObject jsonObj){
		JSONArray eleArrayJson = jsonObj.optJSONArray(HAPDefinitionExpressionGroup.ELEMENT);
		if(eleArrayJson!=null) {
			for(int i=0; i<eleArrayJson.length(); i++) {
				JSONObject expressionJsonObj = eleArrayJson.getJSONObject(i);
				Object expressionObj = expressionJsonObj.opt(HAPDefinitionExpression.EXPRESSION);
				if(expressionObj!=null) {
					//process
					expressionGroup.addEntityElement(HAPParserExpressionDefinition.parseExpressionDefinition(expressionJsonObj));
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
				expressionGroup.addEntityElement(HAPParserExpressionDefinition.parseExpressionDefinition(expressionObj));
			}
			else {
				//reference
//				out.addProcess(id, parseProcessReference(processObjJson));
			}
		}
		
	}
	
	public static HAPDefinitionExpression parseExpressionDefinition(Object obj) {
		HAPDefinitionExpression out = null;
		if(obj instanceof String) {
			out = new HAPDefinitionExpression((String)obj);
		}
		else if(obj instanceof JSONObject) {
			out = new HAPDefinitionExpression();
			out.buildObject(obj, HAPSerializationFormat.JSON);
		}
		return out;
	}
}
