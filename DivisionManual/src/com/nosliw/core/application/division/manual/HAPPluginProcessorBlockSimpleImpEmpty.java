package com.nosliw.core.application.division.manual;

import com.nosliw.core.application.HAPBrickBlockSimple;
import com.nosliw.core.application.HAPIdBrickType;

public class HAPPluginProcessorBlockSimpleImpEmpty extends HAPPluginProcessorBlockSimpleImp{

	public HAPPluginProcessorBlockSimpleImpEmpty(HAPIdBrickType brickTypeId) {
		super(brickTypeId);
	}

	@Override
	public void process(HAPBrickBlockSimple blockExe, HAPManualBrickBlockSimple blockDef,
			HAPManualContextProcessBrick processContext) {
	}
	

}
