package com.nosliw.core.application.division.manual.brick.valuestructure;

import org.json.JSONObject;

import com.nosliw.core.application.HAPManagerApplicationBrick;
import com.nosliw.core.application.common.structure.HAPUtilityParserStructure;
import com.nosliw.core.application.division.manual.core.HAPManualEnumBrickType;
import com.nosliw.core.application.division.manual.core.HAPManualManagerBrick;
import com.nosliw.core.application.division.manual.core.definition.HAPManualDefinitionBrick;
import com.nosliw.core.application.division.manual.core.definition.HAPManualDefinitionContextParse;
import com.nosliw.core.application.division.manual.core.definition.HAPManualDefinitionPluginParserBrickImpSimple;
import com.nosliw.core.application.entity.datarule.HAPManagerDataRule;

public class HAPManualPluginParserBrickImpValueStructure extends HAPManualDefinitionPluginParserBrickImpSimple{

	private HAPManagerDataRule m_dataRuleMan;
	
	public HAPManualPluginParserBrickImpValueStructure(HAPManualManagerBrick manualDivisionEntityMan, HAPManagerApplicationBrick brickMan, HAPManagerDataRule dataRuleMan) {
		super(HAPManualEnumBrickType.VALUESTRUCTURE_100, HAPManualDefinitionBrickValueStructure.class, manualDivisionEntityMan, brickMan);
		this.m_dataRuleMan = dataRuleMan;
	}

	@Override
	protected void parseDefinitionContentJson(HAPManualDefinitionBrick entityDefinition, Object jsonValue, HAPManualDefinitionContextParse parseContext) {
		HAPManualDefinitionBrickValueStructure manualValueStructure = (HAPManualDefinitionBrickValueStructure)entityDefinition;
		HAPUtilityParserStructure.parseValueStructureJson((JSONObject)jsonValue, manualValueStructure.getValue(), this.m_dataRuleMan); 
	}
	
}
