package com.nosliw.core.application.division.manual;

import com.nosliw.common.path.HAPPath;
import com.nosliw.core.application.HAPBrickBlockComplex;
import com.nosliw.core.application.HAPIdBrickType;

public interface HAPPluginProcessorBlockComplex extends HAPPluginProcessorBlock{

	HAPIdBrickType getBrickType();
	
	void processBrick(HAPPath pathFromRoot, HAPManualContextProcessBrick processContext);

	
	
	
	//supply custom constant value
	void extendConstantValue(HAPBrickBlockComplex complexEntityExecutable, HAPManualContextProcessBrick processContext);
	
	//process definition before value context
	void processValueContext(HAPBrickBlockComplex complexEntityExecutable, HAPManualContextProcessBrick processContext);
	void postProcessValueContext(HAPBrickBlockComplex complexEntityExecutable, HAPManualContextProcessBrick processContext);

	//value context extension, variable resolve
	void processValueContextExtension(HAPBrickBlockComplex complexEntityExecutable, HAPManualContextProcessBrick processContext);
	void postProcessValueContextExtension(HAPBrickBlockComplex complexEntityExecutable, HAPManualContextProcessBrick processContext);
	
	//matcher
	void processValueContextDiscovery(HAPBrickBlockComplex complexEntityExecutable, HAPManualContextProcessBrick processContext);
	void postProcessValueContextDiscovery(HAPBrickBlockComplex complexEntityExecutable, HAPManualContextProcessBrick processContext);
	
	//process definition after value context
	void processEntity(HAPBrickBlockComplex complexEntityExecutable, HAPManualContextProcessBrick processContext);
	void postProcessEntity(HAPBrickBlockComplex complexEntityExecutable, HAPManualContextProcessBrick processContext);

}
