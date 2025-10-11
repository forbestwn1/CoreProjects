package com.nosliw.core.application.division.manual.common.valuecontext;

import org.json.JSONArray;
import org.json.JSONObject;

import com.nosliw.core.application.common.structure.HAPUtilityParserStructure;
import com.nosliw.core.application.division.manual.brick.valuestructure.HAPManualDefinitionBrickValueContext;
import com.nosliw.core.application.division.manual.brick.valuestructure.HAPManualDefinitionBrickWrapperValueStructure;
import com.nosliw.core.application.division.manual.core.HAPManualEnumBrickType;
import com.nosliw.core.application.division.manual.core.definition.HAPManualDefinitionAttributeInBrick;
import com.nosliw.core.application.division.manual.core.definition.HAPManualDefinitionContextParse;
import com.nosliw.core.application.division.manual.core.definition.HAPManualDefinitionUtilityParserBrickFormatJson;

public class HAPManualParserValueContext {

	public static void parseValueContextContentJson(HAPManualDefinitionBrickValueContext valueContext, Object jsonValue, HAPManualDefinitionContextParse parseContext) {

		if(jsonValue instanceof JSONArray) {
			JSONArray partJsonArray = (JSONArray)jsonValue;
			for(int i=0; i<partJsonArray.length(); i++) {
				JSONObject partObj = partJsonArray.getJSONObject(i);
				HAPManualDefinitionBrickWrapperValueStructure valueStructureWrapper = parseValueStructureWrapper(partObj, parseContext);
				valueContext.addValueStructure(valueStructureWrapper);
			}
		}
		else if(jsonValue instanceof JSONObject) {
			HAPManualDefinitionBrickWrapperValueStructure valueStructureWrapper = parseValueStructureWrapper((JSONObject)jsonValue, parseContext);
			valueContext.addValueStructure(valueStructureWrapper);
		}
	}

	private static HAPManualDefinitionBrickWrapperValueStructure parseValueStructureWrapper(JSONObject wrapperObj, HAPManualDefinitionContextParse parseContext) {
		JSONObject valueStructureJsonObj = wrapperObj.optJSONObject(HAPManualDefinitionBrickWrapperValueStructure.VALUESTRUCTURE);
		if(valueStructureJsonObj==null) {
			valueStructureJsonObj = wrapperObj;
		}

		HAPManualDefinitionBrickWrapperValueStructure out = (HAPManualDefinitionBrickWrapperValueStructure)parseContext.getManualBrickManager().newBrickDefinition(HAPManualEnumBrickType.VALUESTRUCTUREWRAPPER_100); 
		HAPManualDefinitionAttributeInBrick valueStructureAttr = HAPManualDefinitionUtilityParserBrickFormatJson.parseAttribute(HAPManualDefinitionBrickWrapperValueStructure.VALUESTRUCTURE, valueStructureJsonObj, HAPManualEnumBrickType.VALUESTRUCTURE_100, null, parseContext);
		out.setAttribute(valueStructureAttr);

		HAPUtilityParserStructure.parseValueStructureWrapper(out, wrapperObj);

		return out;
	}
}
