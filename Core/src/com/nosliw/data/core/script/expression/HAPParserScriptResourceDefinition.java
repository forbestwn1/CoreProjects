package com.nosliw.data.core.script.expression;

import org.json.JSONArray;
import org.json.JSONObject;

import com.nosliw.common.info.HAPEntityInfoUtility;
import com.nosliw.common.serialization.HAPSerializationFormat;
import com.nosliw.data.core.component.HAPUtilityComponentParse;

public class HAPParserScriptResourceDefinition {

	public static HAPResourceDefinitionScriptGroup parseScriptResourceDefinition(JSONObject scriptResourceDefJson) {
		HAPResourceDefinitionScriptGroup out = new HAPResourceDefinitionScriptGroup();
		HAPUtilityComponentParse.parseComplextResourceDefinition(out, scriptResourceDefJson);
		
		JSONArray elementsArrayJson = scriptResourceDefJson.getJSONArray(HAPResourceDefinitionScriptGroup.ELEMENT);
		for(int i=0; i<elementsArrayJson.length(); i++) {
			JSONObject eleJsonObj = elementsArrayJson.getJSONObject(i);
			if(HAPEntityInfoUtility.isEnabled(eleJsonObj)) {
				HAPDefinitionScript element = new HAPDefinitionScript();
				element.buildObject(eleJsonObj, HAPSerializationFormat.JSON);
				out.addElement(element);
			}
		}
		return out;
	}
	
}
