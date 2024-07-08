package com.nosliw.core.application.division.manual;

import com.nosliw.core.application.HAPIdBrickType;
import com.nosliw.core.application.division.manual.definition.HAPManualDefinitionBrickBlockSimple;
import com.nosliw.core.application.division.manual.executable.HAPManualBrickBlockSimple;

public class HAPPluginProcessorBlockSimpleImpEmpty extends HAPPluginProcessorBlockSimpleImp{

	public HAPPluginProcessorBlockSimpleImpEmpty(HAPIdBrickType brickTypeId) {
		super(brickTypeId);
	}

	@Override
	public void process(HAPManualBrickBlockSimple blockExe, HAPManualDefinitionBrickBlockSimple blockDef,
			HAPManualContextProcessBrick processContext) {
	}
	

}
