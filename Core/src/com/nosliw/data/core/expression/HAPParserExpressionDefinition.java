package com.nosliw.data.core.expression;

import org.json.JSONObject;

import com.nosliw.common.serialization.HAPSerializationFormat;

public class HAPParserExpressionDefinition {

	
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
