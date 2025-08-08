package com.nosliw.core.application.division.manual.brick.valuestructure;

import org.json.JSONArray;
import org.json.JSONObject;

import com.nosliw.core.application.HAPManagerApplicationBrick;
import com.nosliw.core.application.common.structure22.HAPUtilityParserStructure;
import com.nosliw.core.application.division.manual.core.HAPManualEnumBrickType;
import com.nosliw.core.application.division.manual.core.HAPManualManagerBrick;
import com.nosliw.core.application.division.manual.core.definition.HAPManualDefinitionAttributeInBrick;
import com.nosliw.core.application.division.manual.core.definition.HAPManualDefinitionBrick;
import com.nosliw.core.application.division.manual.core.definition.HAPManualDefinitionContextParse;
import com.nosliw.core.application.division.manual.core.definition.HAPManualDefinitionPluginParserBrickImpSimple;
import com.nosliw.core.application.division.manual.core.definition.HAPManualDefinitionUtilityParserBrickFormatJson;

public class HAPManualPluginParserBrickImpValueContext extends HAPManualDefinitionPluginParserBrickImpSimple{

	public HAPManualPluginParserBrickImpValueContext(HAPManualManagerBrick manualDivisionEntityMan, HAPManagerApplicationBrick brickMan) {
		super(HAPManualEnumBrickType.VALUECONTEXT_100, HAPManualDefinitionBrickValueContext.class, manualDivisionEntityMan, brickMan);
	}
	
	@Override
	protected void parseDefinitionContentJson(HAPManualDefinitionBrick entityDefinition, Object jsonValue, HAPManualDefinitionContextParse parseContext) {
		HAPManualDefinitionBrickValueContext valueContext = (HAPManualDefinitionBrickValueContext)entityDefinition;

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

	private HAPManualDefinitionBrickWrapperValueStructure parseValueStructureWrapper(JSONObject wrapperObj, HAPManualDefinitionContextParse parseContext) {
		JSONObject valueStructureJsonObj = wrapperObj.optJSONObject(HAPManualDefinitionBrickWrapperValueStructure.VALUESTRUCTURE);
		if(valueStructureJsonObj==null) {
			valueStructureJsonObj = wrapperObj;
		}

		HAPManualDefinitionBrickWrapperValueStructure out = (HAPManualDefinitionBrickWrapperValueStructure)this.getManualDivisionBrickManager().newBrickDefinition(HAPManualEnumBrickType.VALUESTRUCTUREWRAPPER_100); 
		HAPManualDefinitionAttributeInBrick valueStructureAttr = HAPManualDefinitionUtilityParserBrickFormatJson.parseAttribute(HAPManualDefinitionBrickWrapperValueStructure.VALUESTRUCTURE, valueStructureJsonObj, HAPManualEnumBrickType.VALUESTRUCTURE_100, null, parseContext);
		out.setAttribute(valueStructureAttr);

		HAPUtilityParserStructure.parseValueStructureWrapper(out, wrapperObj);

		return out;
	}
	
}
