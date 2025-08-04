package com.nosliw.core.application.division.manual.definition;

import com.nosliw.core.application.HAPIdBrickType;
import com.nosliw.core.application.division.manua.HAPManualManagerBrick;
import com.nosliw.data.core.runtime.HAPRuntimeEnvironment;

public class HAPManualDefinitionPluginParserBrickImpSimple extends HAPManualDefinitionPluginParserBrickImp{

	public HAPManualDefinitionPluginParserBrickImpSimple(HAPIdBrickType brickTypeId, Class<? extends HAPManualDefinitionBrick> brickClass,
			HAPManualManagerBrick manualDivisionEntityMan, HAPRuntimeEnvironment runtimeEnv) {
		super(brickTypeId, brickClass, manualDivisionEntityMan, runtimeEnv);
	}
}
