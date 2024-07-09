package com.nosliw.core.application.division.manual;

import com.nosliw.core.application.HAPIdBrickType;
import com.nosliw.core.application.division.manual.definition.HAPManualDefinitionBrickAdapter;
import com.nosliw.core.application.division.manual.executable.HAPManualBrick;
import com.nosliw.core.application.division.manual.executable.HAPManualBrickAdapter;
import com.nosliw.data.core.runtime.HAPRuntimeEnvironment;

public abstract class HAPPluginProcessorAdapter extends HAPPluginProcessorBrick{

	public HAPPluginProcessorAdapter(HAPIdBrickType brickType, Class<? extends HAPManualBrick> brickClass, HAPRuntimeEnvironment runtimeEnv) {
		super(brickType, brickClass, runtimeEnv);
	}

	//process
	public abstract void process(HAPManualBrickAdapter adapterExe, HAPManualDefinitionBrickAdapter adapterDef, HAPManualContextProcessAdapter processContext);

}
