package com.nosliw.core.application.division.manual;

import com.nosliw.core.application.HAPIdBrickType;
import com.nosliw.core.application.division.manual.definition.HAPManualDefinitionBrickBlockSimple;
import com.nosliw.core.application.division.manual.executable.HAPManualBrick;
import com.nosliw.core.application.division.manual.executable.HAPManualBrickBlockSimple;

public class HAPPluginProcessorBlockSimpleImpEmpty extends HAPPluginProcessorBlockSimple{

	public HAPPluginProcessorBlockSimpleImpEmpty(HAPIdBrickType brickTypeId, Class<HAPManualBrick> brickClass) {
		super(brickTypeId, brickClass);
	}

	@Override
	public void process(HAPManualBrickBlockSimple blockExe, HAPManualDefinitionBrickBlockSimple blockDef,
			HAPManualContextProcessBrick processContext) {
	}
	
}
