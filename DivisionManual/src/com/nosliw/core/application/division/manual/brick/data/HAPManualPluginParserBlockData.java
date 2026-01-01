package com.nosliw.core.application.division.manual.brick.data;

import com.nosliw.core.application.brick.HAPEnumBrickType;
import com.nosliw.core.application.division.manual.core.HAPManualManagerBrick;
import com.nosliw.core.application.division.manual.core.definition.HAPManualDefinitionBrick;
import com.nosliw.core.application.division.manual.core.definition.HAPManualDefinitionContextParse;
import com.nosliw.core.application.division.manual.core.definition.HAPManualDefinitionPluginParserBrickImpSimple;
import com.nosliw.core.data.HAPData;
import com.nosliw.core.data.HAPUtilityData;
import com.nosliw.core.xxx.runtimeenv.HAPRuntimeEnvironment;

public class HAPManualPluginParserBlockData extends HAPManualDefinitionPluginParserBrickImpSimple{

	public HAPManualPluginParserBlockData(HAPManualManagerBrick manualDivisionEntityMan, HAPRuntimeEnvironment runtimeEnv) {
		super(HAPEnumBrickType.DATA_100, HAPManualDefinitionBlockData.class, manualDivisionEntityMan, runtimeEnv);
	}

	@Override
	protected void parseSimpleDefinitionContentJson(HAPManualDefinitionBrick brickManual, Object jsonValue, HAPManualDefinitionContextParse parseContext) {
		HAPManualDefinitionBlockData dataBrick = (HAPManualDefinitionBlockData)brickManual;
		HAPData data = HAPUtilityData.buildDataWrapperFromObject(jsonValue);
		dataBrick.setData(data);
	}
}
