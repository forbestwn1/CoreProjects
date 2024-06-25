package com.nosliw.core.application.division.manual;

import com.nosliw.common.path.HAPPath;
import com.nosliw.core.application.HAPBrickBlockComplex;

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
	void extendConstantValue(HAPBrickBlockComplex complexEntityExecutable, HAPManualContextProcessBrick processContext);
	
	//matcher
	void processValueContextDiscovery(HAPBrickBlockComplex complexEntityExecutable, HAPManualContextProcessBrick processContext);
	void postProcessValueContextDiscovery(HAPBrickBlockComplex complexEntityExecutable, HAPManualContextProcessBrick processContext);
	
	//process entity after value context
	void processEntity(HAPBrickBlockComplex complexEntityExecutable, HAPManualContextProcessBrick processContext);
	void postProcessEntity(HAPBrickBlockComplex complexEntityExecutable, HAPManualContextProcessBrick processContext);

}
