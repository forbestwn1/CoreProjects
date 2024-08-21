package com.nosliw.core.application.division.manual;

import com.nosliw.core.application.HAPIdBrickType;
import com.nosliw.core.application.division.manual.definition.HAPManualDefinitionBrick;
import com.nosliw.core.application.division.manual.executable.HAPManualBrick;
import com.nosliw.data.core.runtime.HAPRuntimeEnvironment;

public abstract class HAPManualPluginProcessorAdapter extends HAPManualPluginProcessorBrick{

	public HAPManualPluginProcessorAdapter(HAPIdBrickType brickType, Class<? extends HAPManualBrick> brickClass, HAPRuntimeEnvironment runtimeEnv, HAPManualManagerBrick manualBrickMan) {
		super(brickType, brickClass, runtimeEnv, manualBrickMan);
	}

	//process
	public abstract void process(HAPManualBrick adapterExe, HAPManualDefinitionBrick adapterDef, HAPManualContextProcessAdapter processContext);

}
