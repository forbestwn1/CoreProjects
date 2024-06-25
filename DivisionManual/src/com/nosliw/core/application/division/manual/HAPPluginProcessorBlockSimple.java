package com.nosliw.core.application.division.manual;

import com.nosliw.core.application.HAPBrickBlockSimple;

public interface HAPPluginProcessorBlockSimple extends HAPPluginProcessorBlock{

	//process
	void process(HAPBrickBlockSimple blockExe, HAPManualBrickBlockSimple blockDef, HAPManualContextProcessBrick processContext);

	void postProcess(HAPBrickBlockSimple blockExe, HAPManualBrickBlockSimple blockDef, HAPManualContextProcessBrick processContext);

}
