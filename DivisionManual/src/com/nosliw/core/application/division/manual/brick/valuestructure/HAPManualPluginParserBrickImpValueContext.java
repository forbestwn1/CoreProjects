package com.nosliw.core.application.division.manual.brick.valuestructure;

import org.json.JSONArray;
import org.json.JSONObject;

import com.nosliw.core.application.common.structure.HAPUtilityValueStructureParser;
import com.nosliw.core.application.division.manual.HAPManualEnumBrickType;
import com.nosliw.core.application.division.manual.HAPManualManagerBrick;
import com.nosliw.core.application.division.manual.definition.HAPManualDefinitionAttributeInBrick;
import com.nosliw.core.application.division.manual.definition.HAPManualDefinitionBrick;
import com.nosliw.core.application.division.manual.definition.HAPManualDefinitionContextParse;
import com.nosliw.core.application.division.manual.definition.HAPManualDefinitionPluginParserBrickImpSimple;
import com.nosliw.core.application.division.manual.definition.HAPManualDefinitionUtilityParserBrickFormatJson;
import com.nosliw.data.core.runtime.HAPRuntimeEnvironment;

public class HAPManualPluginParserBrickImpValueContext extends HAPManualDefinitionPluginParserBrickImpSimple{

	public HAPManualPluginParserBrickImpValueContext(HAPManualManagerBrick manualDivisionEntityMan, HAPRuntimeEnvironment runtimeEnv) {
		super(HAPManualEnumBrickType.VALUECONTEXT_100, HAPManualBrickValueContext.class, manualDivisionEntityMan, runtimeEnv);
	}
	
	@Override
	protected void parseDefinitionContentJson(HAPManualDefinitionBrick entityDefinition, Object jsonValue, HAPManualDefinitionContextParse parseContext) {
		HAPManualBrickValueContext valueContext = (HAPManualBrickValueContext)entityDefinition;

		if(jsonValue instanceof JSONArray) {
			JSONArray partJsonArray = (JSONArray)jsonValue;
			for(int i=0; i<partJsonArray.length(); i++) {
				JSONObject partObj = partJsonArray.getJSONObject(i);
				HAPManualBrickWrapperValueStructure valueStructureWrapper = parseValueStructureWrapper(partObj, parseContext);
				valueContext.addValueStructure(valueStructureWrapper);
			}
		}
		else if(jsonValue instanceof JSONObject) {
			HAPManualBrickWrapperValueStructure valueStructureWrapper = parseValueStructureWrapper((JSONObject)jsonValue, parseContext);
			valueContext.addValueStructure(valueStructureWrapper);
		}
	}

	private HAPManualBrickWrapperValueStructure parseValueStructureWrapper(JSONObject wrapperObj, HAPManualDefinitionContextParse parseContext) {
		JSONObject valueStructureJsonObj = wrapperObj.optJSONObject(HAPManualBrickWrapperValueStructure.VALUESTRUCTURE);
		if(valueStructureJsonObj==null) {
			valueStructureJsonObj = wrapperObj;
		}

		HAPManualBrickWrapperValueStructure out = (HAPManualBrickWrapperValueStructure)this.getManualDivisionEntityManager().newBrickDefinition(HAPManualEnumBrickType.VALUESTRUCTUREWRAPPER_100); 
		HAPManualDefinitionAttributeInBrick valueStructureAttr = HAPManualDefinitionUtilityParserBrickFormatJson.parseAttribute(HAPManualBrickWrapperValueStructure.VALUESTRUCTURE, valueStructureJsonObj, HAPManualEnumBrickType.VALUESTRUCTURE_100, null, parseContext, this.getManualDivisionEntityManager(), this.getBrickManager());
		out.setAttribute(valueStructureAttr);

		HAPUtilityValueStructureParser.parseValueStructureWrapper(out, wrapperObj);

		return out;
	}
	
}
