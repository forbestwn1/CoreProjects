package com.nosliw.data.core.resource.external;

import org.json.JSONArray;
import org.json.JSONObject;

import com.nosliw.common.serialization.HAPSerializationFormat;

public class HAPParserExternalDefinition {

	public static HAPDefinitionExternal parse(JSONObject externalDefJson) {
		HAPDefinitionExternal out = new HAPDefinitionExternal();
		
		for(Object key : externalDefJson.keySet()) {
			String type = (String)key;
			JSONArray byNameArray = externalDefJson.getJSONArray(type);
			for(int i=0; i<byNameArray.length(); i++) {
				HAPDefinitionExternalEle ele = new HAPDefinitionExternalEle(type);
				ele.buildObject(byNameArray.getJSONObject(i), HAPSerializationFormat.JSON);
				out.addElement(type, ele);
			}
		}
		
		return out;
	}
	
	
}
