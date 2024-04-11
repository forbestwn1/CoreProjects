package com.nosliw.core.application.division.manual.brick.service.provider;

import com.nosliw.common.serialization.HAPSerializationFormat;
import com.nosliw.core.application.HAPEnumBrickType;
import com.nosliw.core.application.division.manual.HAPManualBrick;
import com.nosliw.core.application.division.manual.HAPManualContextParse;
import com.nosliw.core.application.division.manual.HAPManualManagerBrick;
import com.nosliw.core.application.division.manual.HAPPluginParserBrickImpSimple;
import com.nosliw.data.core.runtime.HAPRuntimeEnvironment;

public class HAPPluginParserBrickImpServiceProvider extends HAPPluginParserBrickImpSimple{

	public HAPPluginParserBrickImpServiceProvider(HAPManualManagerBrick manualDivisionEntityMan, HAPRuntimeEnvironment runtimeEnv) {
		super(HAPEnumBrickType.SERVICEPROVIDER_100, HAPDefinitionEntityInDomainServiceProvider.class, manualDivisionEntityMan, runtimeEnv);
	}
	
	@Override
	protected void parseDefinitionContentJson(HAPManualBrick entityDefinition, Object jsonValue, HAPManualContextParse parseContext) {
		HAPDefinitionEntityInDomainServiceProvider entity = (HAPDefinitionEntityInDomainServiceProvider)entityDefinition;
		entity.buildObject(jsonValue, HAPSerializationFormat.JSON);
	}
}
