package com.nosliw.core.application.division.manual;

import com.nosliw.core.application.HAPIdBrickType;
import com.nosliw.core.application.division.manual.definition.HAPManualDefinitionBrickAdapter;
import com.nosliw.core.application.division.manual.executable.HAPManualBrick;
import com.nosliw.data.core.runtime.HAPRuntimeEnvironment;

public abstract class HAPPluginProcessorAdapter extends HAPPluginProcessorBrick{

	public HAPPluginProcessorAdapter(HAPIdBrickType brickType, Class<? extends HAPManualBrick> brickClass, HAPRuntimeEnvironment runtimeEnv, HAPManualManagerBrick manualBrickMan) {
		super(brickType, brickClass, runtimeEnv, manualBrickMan);
	}

	//process
	public abstract void process(HAPManualBrick adapterExe, HAPManualDefinitionBrickAdapter adapterDef, HAPManualContextProcessAdapter processContext);

}
