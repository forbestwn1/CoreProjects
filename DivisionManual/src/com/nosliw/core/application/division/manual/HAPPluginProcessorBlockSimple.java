package com.nosliw.core.application.division.manual;

import com.nosliw.core.application.division.manual.definition.HAPManualDefinitionBrickBlockSimple;
import com.nosliw.core.application.division.manual.executable.HAPManualBrickBlockSimple;

public interface HAPPluginProcessorBlockSimple extends HAPPluginProcessorBlock{

	//process
	void process(HAPManualBrickBlockSimple blockExe, HAPManualDefinitionBrickBlockSimple blockDef, HAPManualContextProcessBrick processContext);

	void postProcess(HAPManualBrickBlockSimple blockExe, HAPManualDefinitionBrickBlockSimple blockDef, HAPManualContextProcessBrick processContext);

}
