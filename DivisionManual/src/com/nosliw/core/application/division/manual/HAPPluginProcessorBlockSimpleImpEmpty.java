package com.nosliw.core.application.division.manual;

import com.nosliw.core.application.HAPIdBrickType;
import com.nosliw.core.application.division.manual.definition.HAPManualDefinitionBrickBlockSimple;
import com.nosliw.core.application.division.manual.executable.HAPManualBrick;
import com.nosliw.core.application.division.manual.executable.HAPManualBrickBlockSimple;
import com.nosliw.data.core.runtime.HAPRuntimeEnvironment;

public class HAPPluginProcessorBlockSimpleImpEmpty extends HAPPluginProcessorBlockSimple{

	public HAPPluginProcessorBlockSimpleImpEmpty(HAPIdBrickType brickTypeId, Class<HAPManualBrick> brickClass, HAPRuntimeEnvironment runtimeEnv) {
		super(brickTypeId, brickClass, runtimeEnv);
	}

	@Override
	public void process(HAPManualBrickBlockSimple blockExe, HAPManualDefinitionBrickBlockSimple blockDef,
			HAPManualContextProcessBrick processContext) {
	}
	
}
