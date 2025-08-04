package com.nosliw.core.application.division.manual.brick.module;

import com.nosliw.core.application.division.manua.HAPManualManagerBrick;
import com.nosliw.core.application.division.manua.HAPManualPluginProcessorBlockComplex;
import com.nosliw.core.xxx.application1.brick.HAPEnumBrickType;
import com.nosliw.data.core.runtime.HAPRuntimeEnvironment;

public class HAPManualPluginProcessorBlockModule extends HAPManualPluginProcessorBlockComplex{

	public HAPManualPluginProcessorBlockModule(HAPRuntimeEnvironment runtimeEnv, HAPManualManagerBrick manualBrickMan) {
		super(HAPEnumBrickType.MODULE_100, HAPManualBlockModule.class, runtimeEnv, manualBrickMan);
	}

}
