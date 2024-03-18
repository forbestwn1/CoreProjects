package com.nosliw.core.application.division.manual;

import com.nosliw.core.application.HAPIdBrickType;
import com.nosliw.data.core.runtime.HAPRuntimeEnvironment;

public class HAPPluginParserBrickImpSimple extends HAPPluginParserBrickImp{

	public HAPPluginParserBrickImpSimple(HAPIdBrickType brickTypeId, Class<? extends HAPManualBrick> brickClass,
			HAPManualManagerBrick manualDivisionEntityMan, HAPRuntimeEnvironment runtimeEnv) {
		super(brickTypeId, brickClass, manualDivisionEntityMan, runtimeEnv);
	}
}
