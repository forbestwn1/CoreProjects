package com.nosliw.data.core.script.expression.resource;

import org.json.JSONArray;
import org.json.JSONObject;

import com.nosliw.common.info.HAPUtilityEntityInfo;
import com.nosliw.common.serialization.HAPSerializationFormat;
import com.nosliw.data.core.component.HAPParserComponent;
import com.nosliw.data.core.resource.HAPParserResourceDefinitionImp;
import com.nosliw.data.core.resource.HAPResourceDefinition;
import com.nosliw.data.core.script.expression.HAPDefinitionScriptEntity;

public class HAPParserResourceDefinitionScriptGroup extends HAPParserResourceDefinitionImp{

	@Override
	public HAPResourceDefinition parseJson(JSONObject scriptResourceDefJson) {
		HAPResourceDefinitionScriptGroup out = new HAPResourceDefinitionScriptGroup();
		HAPParserComponent.parseComplextResourceDefinition(out, scriptResourceDefJson);
		
		JSONArray elementsArrayJson = scriptResourceDefJson.getJSONArray(HAPResourceDefinitionScriptGroup.ELEMENT);
		for(int i=0; i<elementsArrayJson.length(); i++) {
			JSONObject eleJsonObj = elementsArrayJson.getJSONObject(i);
			if(HAPUtilityEntityInfo.isEnabled(eleJsonObj)) {
				HAPDefinitionScriptEntity element = new HAPDefinitionScriptEntity();
				element.buildObject(eleJsonObj, HAPSerializationFormat.JSON);
				out.addEntityElement(element);
			}
		}
		return out;
	}
	
}
