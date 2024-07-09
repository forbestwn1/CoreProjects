package com.nosliw.core.application.division.manual;

import com.nosliw.core.application.HAPIdBrickType;
import com.nosliw.core.application.division.manual.definition.HAPManualDefinitionBrickBlockSimple;
import com.nosliw.core.application.division.manual.executable.HAPManualBrick;
import com.nosliw.core.application.division.manual.executable.HAPManualBrickBlockSimple;
import com.nosliw.data.core.runtime.HAPRuntimeEnvironment;

public class HAPPluginProcessorBlockSimpleImpEmpty extends HAPPluginProcessorBlockSimple{

	public HAPPluginProcessorBlockSimpleImpEmpty(HAPIdBrickType brickTypeId, Class<? extends HAPManualBrick> brickClass, HAPRuntimeEnvironment runtimeEnv, HAPManualManagerBrick manualBrickMan) {
		super(brickTypeId, brickClass, runtimeEnv, manualBrickMan);
	}

	@Override
	public void process(HAPManualBrickBlockSimple blockExe, HAPManualDefinitionBrickBlockSimple blockDef,
			HAPManualContextProcessBrick processContext) {
	}
	
}
