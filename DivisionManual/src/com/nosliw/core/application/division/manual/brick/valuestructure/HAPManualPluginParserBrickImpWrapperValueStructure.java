package com.nosliw.core.application.division.manual.brick.valuestructure;

import com.nosliw.core.application.HAPIdBrickType;
import com.nosliw.core.application.division.manual.HAPManualBrick;
import com.nosliw.core.application.division.manual.HAPManualManagerBrick;
import com.nosliw.core.application.division.manual.HAPPluginParserBrickImpSimple;
import com.nosliw.data.core.runtime.HAPRuntimeEnvironment;

public class HAPManualPluginParserBrickImpWrapperValueStructure extends HAPPluginParserBrickImpSimple{

	public HAPManualPluginParserBrickImpWrapperValueStructure(HAPIdBrickType brickTypeId,
			Class<? extends HAPManualBrick> brickClass, HAPManualManagerBrick manualDivisionEntityMan,
			HAPRuntimeEnvironment runtimeEnv) {
		super(brickTypeId, brickClass, manualDivisionEntityMan, runtimeEnv);
		// TODO Auto-generated constructor stub
	}

}
