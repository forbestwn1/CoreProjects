package com.nosliw.core.application.division.manual;

import com.nosliw.core.application.HAPBundle;
import com.nosliw.data.core.runtime.HAPRuntimeEnvironment;

public class HAPManualContextProcessBrick extends HAPManualContextProcess{

	public HAPManualContextProcessBrick(HAPBundle bundle, String rootBrickName, HAPRuntimeEnvironment runtimeEnv, HAPManualManagerBrick manualBrickMan) {
		super(bundle, rootBrickName, runtimeEnv, manualBrickMan);
	}
	
}
