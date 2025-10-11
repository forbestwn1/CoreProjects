package com.nosliw.core.application.division.manual.brick.valuestructure;

import com.nosliw.core.application.HAPManagerApplicationBrick;
import com.nosliw.core.application.division.manual.common.valuecontext.HAPManualParserValueContext;
import com.nosliw.core.application.division.manual.core.HAPManualEnumBrickType;
import com.nosliw.core.application.division.manual.core.HAPManualManagerBrick;
import com.nosliw.core.application.division.manual.core.definition.HAPManualDefinitionBrick;
import com.nosliw.core.application.division.manual.core.definition.HAPManualDefinitionContextParse;
import com.nosliw.core.application.division.manual.core.definition.HAPManualDefinitionPluginParserBrickImpSimple;

public class HAPManualPluginParserBrickImpValueContext extends HAPManualDefinitionPluginParserBrickImpSimple{

	public HAPManualPluginParserBrickImpValueContext(HAPManualManagerBrick manualDivisionEntityMan, HAPManagerApplicationBrick brickMan) {
		super(HAPManualEnumBrickType.VALUECONTEXT_100, HAPManualDefinitionBrickValueContext.class, manualDivisionEntityMan, brickMan);
	}
	
	@Override
	protected void parseDefinitionContentJson(HAPManualDefinitionBrick entityDefinition, Object jsonValue, HAPManualDefinitionContextParse parseContext) {
		HAPManualParserValueContext.parseValueContextContentJson((HAPManualDefinitionBrickValueContext)entityDefinition, jsonValue, parseContext);
	}
}
