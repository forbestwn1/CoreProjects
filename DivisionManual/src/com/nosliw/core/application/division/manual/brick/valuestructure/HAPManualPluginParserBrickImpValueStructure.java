package com.nosliw.core.application.division.manual.brick.valuestructure;

import org.json.JSONObject;

import com.nosliw.core.application.common.structure.HAPUtilityValueStructureParser;
import com.nosliw.core.application.division.manual.HAPManualEnumBrickType;
import com.nosliw.core.application.division.manual.HAPManualManagerBrick;
import com.nosliw.core.application.division.manual.definition.HAPManualDefinitionBrick;
import com.nosliw.core.application.division.manual.definition.HAPManualDefinitionContextParse;
import com.nosliw.core.application.division.manual.definition.HAPManualDefinitionPluginParserBrickImpSimple;
import com.nosliw.data.core.runtime.HAPRuntimeEnvironment;

public class HAPManualPluginParserBrickImpValueStructure extends HAPManualDefinitionPluginParserBrickImpSimple{

	public HAPManualPluginParserBrickImpValueStructure(HAPManualManagerBrick manualDivisionEntityMan, HAPRuntimeEnvironment runtimeEnv) {
		super(HAPManualEnumBrickType.VALUESTRUCTURE_100, HAPManualBrickValueStructure.class, manualDivisionEntityMan, runtimeEnv);
	}

	@Override
	protected void parseDefinitionContentJson(HAPManualDefinitionBrick entityDefinition, Object jsonValue, HAPManualDefinitionContextParse parseContext) {
		HAPManualBrickValueStructure manualValueStructure = (HAPManualBrickValueStructure)entityDefinition;
		HAPUtilityValueStructureParser.parseValueStructureJson((JSONObject)jsonValue, manualValueStructure.getValue()); 
	}
	
}
