package com.nosliw.data.core.expression;

import org.json.JSONArray;
import org.json.JSONObject;

import com.nosliw.common.serialization.HAPSerializationFormat;
import com.nosliw.data.core.component.HAPUtilityComponentParse;
import com.nosliw.data.core.component.attachment.HAPAttachment;
import com.nosliw.data.core.expression.resource.HAPDefinitionResourceDefinitionExpressionSuiteElementEntity;

public class HAPParserExpressionDefinition {

	public static HAPDefinitionExpressionGroup parseExpressionGroupDefinition(HAPAttachment attachment, HAPParserExpression expressionParser) {
		HAPDefinitionExpressionGroupImp out = new HAPDefinitionExpressionGroupImp();
		
		
		return out;
	}
	
	public static HAPDefinitionResourceDefinitionExpressionSuiteElementEntity parseExpressionSuiteElement(JSONObject jsonObj, HAPParserExpression expressionParser) {
		HAPDefinitionResourceDefinitionExpressionSuiteElementEntity out = new HAPDefinitionResourceDefinitionExpressionSuiteElementEntity();
		HAPUtilityComponentParse.parseComponent(out, jsonObj);

		JSONArray eleArrayJson = jsonObj.optJSONArray(HAPDefinitionResourceDefinitionExpressionSuiteElementEntity.ELEMENT);
		if(eleArrayJson!=null) {
			for(int i=0; i<eleArrayJson.length(); i++) {
				JSONObject expressionJsonObj = eleArrayJson.getJSONObject(i);
				Object expressionObj = expressionJsonObj.opt(HAPDefinitionExpression.EXPRESSION);
				if(expressionObj!=null) {
					//process
					out.addEntityElement(parseExpressionDefinition(expressionJsonObj, expressionParser));
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
				out.addEntityElement(parseExpressionDefinition(jsonObj, expressionParser));
			}
			else {
				//reference
//				out.addProcess(id, parseProcessReference(processObjJson));
			}
		}
		return out;
	}

	public static HAPDefinitionExpression parseExpressionDefinition(JSONObject jsonObj, HAPParserExpression expressionParser) {
		HAPDefinitionExpression out = new HAPDefinitionExpression();
		out.buildObject(jsonObj, HAPSerializationFormat.JSON);
//		out.buildEntityInfoByJson(jsonObj);
//		String expressionStr = jsonObj.getString(HAPDefinitionExpression.EXPRESSION);
//		out.setOperand(expressionParser.parseExpression(expressionStr));
//		
//		JSONArray refJsonArray = jsonObj.optJSONArray(HAPDefinitionExpression.REFERENCEMAPPING);
//		if(refJsonArray!=null) {
//			for(int i=0; i<refJsonArray.length(); i++) {
//				JSONObject refJsonObj = refJsonArray.getJSONObject(i);
//				HAPDefinitionReference ref = new HAPDefinitionReference();
//				ref.buildObject(refJsonObj, HAPSerializationFormat.JSON);
//				out.addReference(ref);
//			}
//		}
		return out;
	}

}
