package com.nosliw.core.application.division.manual;

import com.nosliw.core.application.HAPIdBrickType;
import com.nosliw.core.application.division.manual.executable.HAPManualBrick;
import com.nosliw.data.core.runtime.HAPRuntimeEnvironment;

public class HAPManualPluginProcessorBlockSimpleImpEmpty extends HAPManualPluginProcessorBlockComplex{

	public HAPManualPluginProcessorBlockSimpleImpEmpty(HAPIdBrickType brickTypeId, Class<? extends HAPManualBrick> brickClass, HAPRuntimeEnvironment runtimeEnv, HAPManualManagerBrick manualBrickMan) {
		super(brickTypeId, brickClass, runtimeEnv, manualBrickMan);
	}

}
