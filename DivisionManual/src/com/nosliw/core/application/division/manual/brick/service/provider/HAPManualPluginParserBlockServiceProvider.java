package com.nosliw.core.application.division.manual.brick.service.provider;

import com.nosliw.common.serialization.HAPSerializationFormat;
import com.nosliw.core.application.division.manual.core.HAPManualManagerBrick;
import com.nosliw.core.application.division.manual.core.definition.HAPManualDefinitionBrick;
import com.nosliw.core.application.division.manual.core.definition.HAPManualDefinitionContextParse;
import com.nosliw.core.application.division.manual.core.definition1.HAPManualDefinitionPluginParserBrickImpSimple;
import com.nosliw.core.xxx.application1.brick.HAPEnumBrickType;
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
