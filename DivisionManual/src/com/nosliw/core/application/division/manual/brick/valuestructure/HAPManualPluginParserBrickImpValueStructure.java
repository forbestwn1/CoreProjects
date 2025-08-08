package com.nosliw.core.application.division.manual.brick.valuestructure;

import org.json.JSONObject;

import com.nosliw.core.application.HAPManagerApplicationBrick;
import com.nosliw.core.application.common.structure.HAPUtilityParserStructure;
import com.nosliw.core.application.division.manual.core.HAPManualEnumBrickType;
import com.nosliw.core.application.division.manual.core.HAPManualManagerBrick;
import com.nosliw.core.application.division.manual.core.definition.HAPManualDefinitionBrick;
import com.nosliw.core.application.division.manual.core.definition.HAPManualDefinitionContextParse;
import com.nosliw.core.application.division.manual.core.definition.HAPManualDefinitionPluginParserBrickImpSimple;

public class HAPManualPluginParserBrickImpValueStructure extends HAPManualDefinitionPluginParserBrickImpSimple{

	public HAPManualPluginParserBrickImpValueStructure(HAPManualManagerBrick manualDivisionEntityMan, HAPManagerApplicationBrick brickMan) {
		super(HAPManualEnumBrickType.VALUESTRUCTURE_100, HAPManualDefinitionBrickValueStructure.class, manualDivisionEntityMan, brickMan);
	}

	@Override
	protected void parseDefinitionContentJson(HAPManualDefinitionBrick entityDefinition, Object jsonValue, HAPManualDefinitionContextParse parseContext) {
		HAPManualDefinitionBrickValueStructure manualValueStructure = (HAPManualDefinitionBrickValueStructure)entityDefinition;
		HAPUtilityParserStructure.parseValueStructureJson((JSONObject)jsonValue, manualValueStructure.getValue()); 
	}
	
}
