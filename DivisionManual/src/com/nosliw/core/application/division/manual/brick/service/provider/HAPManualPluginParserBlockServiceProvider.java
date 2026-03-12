package com.nosliw.core.application.division.manual.brick.service.provider;

import com.nosliw.common.serialization.HAPSerializationFormat;
import com.nosliw.core.application.HAPManagerApplicationBrick;
import com.nosliw.core.application.brick.HAPEnumBrickType;
import com.nosliw.core.application.division.manual.core.HAPManualManagerBrick;
import com.nosliw.core.application.division.manual.core.definition.HAPManualDefinitionBrick;
import com.nosliw.core.application.division.manual.core.definition.HAPManualDefinitionContextParse;
import com.nosliw.core.application.division.manual.core.definition.HAPManualDefinitionPluginParserBrickImpSimple;

public class HAPManualPluginParserBlockServiceProvider extends HAPManualDefinitionPluginParserBrickImpSimple{

	public HAPManualPluginParserBlockServiceProvider(HAPManualManagerBrick manualDivisionEntityMan, HAPManagerApplicationBrick brickMan) {
		super(HAPEnumBrickType.SERVICEPROVIDER_100, HAPManualDefinitionBlockSimpleServiceProvider.class, manualDivisionEntityMan, brickMan);
	}
	
	@Override
	protected void parseSimpleDefinitionContentJson(HAPManualDefinitionBrick entityDefinition, Object jsonValue, HAPManualDefinitionContextParse parseContext) {
		HAPManualDefinitionBlockSimpleServiceProvider entity = (HAPManualDefinitionBlockSimpleServiceProvider)entityDefinition;
		entity.buildObject(jsonValue, HAPSerializationFormat.JSON);
	}
}
