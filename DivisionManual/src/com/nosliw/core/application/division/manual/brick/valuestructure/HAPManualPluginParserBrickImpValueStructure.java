package com.nosliw.core.application.division.manual.brick.valuestructure;

import org.json.JSONObject;

import com.nosliw.core.application.common.structure.HAPUtilityValueStructureParser;
import com.nosliw.core.application.division.manual.core.HAPManualManagerBrick;
import com.nosliw.core.application.division.manual.core.a.HAPManualEnumBrickType;
import com.nosliw.core.application.division.manual.core.definition.HAPManualDefinitionBrick;
import com.nosliw.core.application.division.manual.core.definition.HAPManualDefinitionContextParse;
import com.nosliw.core.application.division.manual.core.definition.HAPManualDefinitionPluginParserBrickImpSimple;
import com.nosliw.data.core.runtime.HAPRuntimeEnvironment;

public class HAPManualPluginParserBrickImpValueStructure extends HAPManualDefinitionPluginParserBrickImpSimple{

	public HAPManualPluginParserBrickImpValueStructure(HAPManualManagerBrick manualDivisionEntityMan, HAPRuntimeEnvironment runtimeEnv) {
		super(HAPManualEnumBrickType.VALUESTRUCTURE_100, HAPManualDefinitionBrickValueStructure.class, manualDivisionEntityMan, runtimeEnv);
	}

	@Override
	protected void parseDefinitionContentJson(HAPManualDefinitionBrick entityDefinition, Object jsonValue, HAPManualDefinitionContextParse parseContext) {
		HAPManualDefinitionBrickValueStructure manualValueStructure = (HAPManualDefinitionBrickValueStructure)entityDefinition;
		HAPUtilityValueStructureParser.parseValueStructureJson((JSONObject)jsonValue, manualValueStructure.getValue()); 
	}
	
}
