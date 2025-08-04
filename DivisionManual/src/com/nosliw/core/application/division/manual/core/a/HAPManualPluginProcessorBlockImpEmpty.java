package com.nosliw.core.application.division.manual.core.a;

import com.nosliw.core.application.HAPIdBrickType;
import com.nosliw.core.application.division.manual.core.HAPManualBrick;
import com.nosliw.core.application.division.manual.core.HAPManualManagerBrick;
import com.nosliw.core.application.division.manual.core.process.HAPManualPluginProcessorBlockComplex;
import com.nosliw.data.core.runtime.HAPRuntimeEnvironment;

public class HAPManualPluginProcessorBlockImpEmpty extends HAPManualPluginProcessorBlockComplex{

	public HAPManualPluginProcessorBlockImpEmpty(HAPIdBrickType brickTypeId, Class<? extends HAPManualBrick> brickClass, HAPRuntimeEnvironment runtimeEnv, HAPManualManagerBrick manualBrickMan) {
		super(brickTypeId, brickClass, runtimeEnv, manualBrickMan);
	}

}
