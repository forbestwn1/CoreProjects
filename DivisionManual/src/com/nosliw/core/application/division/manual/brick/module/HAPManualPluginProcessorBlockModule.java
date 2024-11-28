package com.nosliw.core.application.division.manual.brick.module;

import com.nosliw.core.application.HAPEnumBrickType;
import com.nosliw.core.application.division.manual.HAPManualManagerBrick;
import com.nosliw.core.application.division.manual.HAPManualPluginProcessorBlockComplex;
import com.nosliw.data.core.runtime.HAPRuntimeEnvironment;

public class HAPManualPluginProcessorBlockModule extends HAPManualPluginProcessorBlockComplex{

	public HAPManualPluginProcessorBlockModule(HAPRuntimeEnvironment runtimeEnv, HAPManualManagerBrick manualBrickMan) {
		super(HAPEnumBrickType.MODULE_100, HAPManualBlockModule.class, runtimeEnv, manualBrickMan);
	}

}
