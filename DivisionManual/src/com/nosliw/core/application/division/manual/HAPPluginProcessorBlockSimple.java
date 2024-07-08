package com.nosliw.core.application.division.manual;

import com.nosliw.core.application.HAPIdBrickType;
import com.nosliw.core.application.division.manual.definition.HAPManualDefinitionBrickBlockSimple;
import com.nosliw.core.application.division.manual.executable.HAPManualBrick;
import com.nosliw.core.application.division.manual.executable.HAPManualBrickBlockSimple;

public abstract class HAPPluginProcessorBlockSimple extends HAPPluginProcessorBlock{

	public HAPPluginProcessorBlockSimple(HAPIdBrickType brickType, Class<HAPManualBrick> brickClass) {
		super(brickType, brickClass);
	}
	
	public void postProcess(HAPManualBrickBlockSimple blockExe, HAPManualDefinitionBrickBlockSimple blockDef, HAPManualContextProcessBrick processContext) {}
	
	public abstract void process(HAPManualBrickBlockSimple blockExe, HAPManualDefinitionBrickBlockSimple blockDef, HAPManualContextProcessBrick processContext);
}
