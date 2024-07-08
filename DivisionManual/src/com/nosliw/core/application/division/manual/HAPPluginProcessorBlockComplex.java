package com.nosliw.core.application.division.manual;

import com.nosliw.common.path.HAPPath;
import com.nosliw.core.application.division.manual.executable.HAPManualBrickBlockComplex;

public interface HAPPluginProcessorBlockComplex extends HAPPluginProcessorBlock{

	//process definition before value context
	void processInit(HAPPath pathFromRoot, HAPManualContextProcessBrick processContext);
	void postProcessInit(HAPPath pathFromRoot, HAPManualContextProcessBrick processContext);

	//value context extension, variable resolve
	void processVariableResolve(HAPPath pathFromRoot, HAPManualContextProcessBrick processContext);
	void postProcessVariableResolve(HAPPath pathFromRoot, HAPManualContextProcessBrick processContext);
	

	
	void processBrick(HAPPath pathFromRoot, HAPManualContextProcessBrick processContext);

	void postProcessBrick(HAPPath pathFromRoot, HAPManualContextProcessBrick processContext);
	
	
	
	//supply custom constant value
	void extendConstantValue(HAPManualBrickBlockComplex complexEntityExecutable, HAPManualContextProcessBrick processContext);
	
	//matcher
	void processValueContextDiscovery(HAPManualBrickBlockComplex complexEntityExecutable, HAPManualContextProcessBrick processContext);
	void postProcessValueContextDiscovery(HAPManualBrickBlockComplex complexEntityExecutable, HAPManualContextProcessBrick processContext);
	
	//process entity after value context
	void processEntity(HAPManualBrickBlockComplex complexEntityExecutable, HAPManualContextProcessBrick processContext);
	void postProcessEntity(HAPManualBrickBlockComplex complexEntityExecutable, HAPManualContextProcessBrick processContext);

}
