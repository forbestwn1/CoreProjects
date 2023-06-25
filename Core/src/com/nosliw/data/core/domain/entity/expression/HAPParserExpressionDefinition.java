package com.nosliw.data.core.domain.entity.expression;

import org.json.JSONObject;

import com.nosliw.common.info.HAPUtilityEntityInfo;
import com.nosliw.common.serialization.HAPSerializationFormat;

public class HAPParserExpressionDefinition {

	
	public static HAPDefinitionExpression parseExpressionDefinition(Object obj) {
		HAPDefinitionExpression out = null;
		if(obj instanceof String) {
			out = new HAPDefinitionExpression((String)obj);
		}
		else if(obj instanceof JSONObject) {
			if(HAPUtilityEntityInfo.isEnabled((JSONObject)obj)) {
				out = new HAPDefinitionExpression();
				out.buildObject(obj, HAPSerializationFormat.JSON);
			}
		}
		return out;
	}
}
