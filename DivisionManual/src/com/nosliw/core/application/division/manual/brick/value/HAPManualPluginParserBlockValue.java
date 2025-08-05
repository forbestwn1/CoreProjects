package com.nosliw.core.application.division.manual.brick.value;

import com.nosliw.core.application.division.manual.core.HAPManualManagerBrick;
import com.nosliw.core.application.division.manual.core.definition.HAPManualDefinitionBrick;
import com.nosliw.core.application.division.manual.core.definition.HAPManualDefinitionContextParse;
import com.nosliw.core.application.division.manual.core.definition.HAPManualDefinitionPluginParserBrickImpSimple;
import com.nosliw.core.xxx.application1.brick.HAPEnumBrickType;
import com.nosliw.data.core.runtime.HAPRuntimeEnvironment;

public class HAPManualPluginParserBlockValue extends HAPManualDefinitionPluginParserBrickImpSimple{

	public HAPManualPluginParserBlockValue(HAPManualManagerBrick manualDivisionEntityMan, HAPRuntimeEnvironment runtimeEnv) {
		super(HAPEnumBrickType.DATA_100, HAPManualDefinitionBlockValue.class, manualDivisionEntityMan, runtimeEnv);
	}

	@Override
	protected void parseDefinitionContentJson(HAPManualDefinitionBrick brickManual, Object jsonValue, HAPManualDefinitionContextParse parseContext) {
		HAPManualDefinitionBlockValue valueBrick = (HAPManualDefinitionBlockValue)brickManual;
		valueBrick.setValue(jsonValue);
	}
}
