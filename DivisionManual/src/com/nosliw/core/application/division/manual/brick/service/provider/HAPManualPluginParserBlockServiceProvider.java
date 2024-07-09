package com.nosliw.core.application.division.manual.brick.service.provider;

import com.nosliw.common.serialization.HAPSerializationFormat;
import com.nosliw.core.application.HAPEnumBrickType;
import com.nosliw.core.application.division.manual.HAPManualManagerBrick;
import com.nosliw.core.application.division.manual.definition.HAPManualDefinitionBrick;
import com.nosliw.core.application.division.manual.definition.HAPManualDefinitionContextParse;
import com.nosliw.core.application.division.manual.definition.HAPManualDefinitionPluginParserBrickImpSimple;
import com.nosliw.data.core.runtime.HAPRuntimeEnvironment;

public class HAPManualPluginParserBlockServiceProvider extends HAPManualDefinitionPluginParserBrickImpSimple{

	public HAPManualPluginParserBlockServiceProvider(HAPManualManagerBrick manualDivisionEntityMan, HAPRuntimeEnvironment runtimeEnv) {
		super(HAPEnumBrickType.SERVICEPROVIDER_100, HAPManualDefinitionBlockSimpleServiceProvider.class, manualDivisionEntityMan, runtimeEnv);
	}
	
	@Override
	protected void parseDefinitionContentJson(HAPManualDefinitionBrick entityDefinition, Object jsonValue, HAPManualDefinitionContextParse parseContext) {
		HAPManualDefinitionBlockSimpleServiceProvider entity = (HAPManualDefinitionBlockSimpleServiceProvider)entityDefinition;
		entity.buildObject(jsonValue, HAPSerializationFormat.JSON);
	}
}
