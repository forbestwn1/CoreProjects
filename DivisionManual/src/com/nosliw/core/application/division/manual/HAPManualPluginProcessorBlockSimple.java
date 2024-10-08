package com.nosliw.core.application.division.manual;

import com.nosliw.core.application.HAPIdBrickType;
import com.nosliw.core.application.division.manual.definition.HAPManualDefinitionBrick;
import com.nosliw.core.application.division.manual.executable.HAPManualBrick;
import com.nosliw.data.core.runtime.HAPRuntimeEnvironment;

public abstract class HAPManualPluginProcessorBlockSimple extends HAPManualPluginProcessorBlock{

	public HAPManualPluginProcessorBlockSimple(HAPIdBrickType brickType, Class<? extends HAPManualBrick> brickClass, HAPRuntimeEnvironment runtimeEnv, HAPManualManagerBrick manualBrickMan) {
		super(brickType, brickClass, runtimeEnv, manualBrickMan);
	}
	
	public void postProcess(HAPManualBrick blockExe, HAPManualDefinitionBrick blockDef, HAPManualContextProcessBrick processContext) {}
	
	public abstract void process(HAPManualBrick blockExe, HAPManualDefinitionBrick blockDef, HAPManualContextProcessBrick processContext);
}
