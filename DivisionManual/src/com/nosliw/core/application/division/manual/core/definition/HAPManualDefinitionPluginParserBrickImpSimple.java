package com.nosliw.core.application.division.manual.core.definition;

import com.nosliw.core.application.HAPIdBrickType;
import com.nosliw.core.application.HAPManagerApplicationBrick;
import com.nosliw.core.application.division.manual.core.HAPManualManagerBrick;

public class HAPManualDefinitionPluginParserBrickImpSimple extends HAPManualDefinitionPluginParserBrickImp{

	public HAPManualDefinitionPluginParserBrickImpSimple(HAPIdBrickType brickTypeId, Class<? extends HAPManualDefinitionBrick> brickClass,
			HAPManualManagerBrick manualDivisionEntityMan, HAPManagerApplicationBrick brickMan) {
		super(brickTypeId, brickClass, manualDivisionEntityMan, brickMan);
	}

	@Override
	protected void parseDefinitionContentJson(HAPManualDefinitionBrick brickDefinition, Object jsonValue, HAPManualDefinitionContextParse parseContext) {
	    super.parseDefinitionContentJson(brickDefinition, jsonValue, parseContext);
	    this.parseSimpleDefinitionContentJson(brickDefinition, jsonValue, parseContext);
	}
	
	protected void parseSimpleDefinitionContentJson(HAPManualDefinitionBrick brickDefinition, Object jsonValue, HAPManualDefinitionContextParse parseContext) {}

}
