package com.nosliw.core.application.division.manual.brick.taskwrapper;

import com.nosliw.core.application.HAPBrickBlockSimple;
import com.nosliw.core.application.HAPEnumBrickType;
import com.nosliw.core.application.division.manual.HAPManualBrickBlockSimple;
import com.nosliw.core.application.division.manual.HAPManualContextProcessBrick;
import com.nosliw.core.application.division.manual.HAPPluginProcessorBlockSimpleImp;

public class HAPManualPluginProcessorBlockSimpleImpTaskWrapper extends HAPPluginProcessorBlockSimpleImp{

	public HAPManualPluginProcessorBlockSimpleImpTaskWrapper() {
		super(HAPEnumBrickType.TASKWRAPPER_100);
	}

	@Override
	public void process(HAPBrickBlockSimple blockExe, HAPManualBrickBlockSimple blockDef, HAPManualContextProcessBrick processContext) {
	}
}
