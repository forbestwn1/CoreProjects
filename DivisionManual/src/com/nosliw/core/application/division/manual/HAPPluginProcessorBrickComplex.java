package com.nosliw.core.application.division.manual;

import com.nosliw.common.path.HAPPath;
import com.nosliw.core.application.HAPBlockComplex;
import com.nosliw.core.application.HAPIdBrickType;

public interface HAPPluginProcessorBrickComplex extends HAPPluginProcessorBlock{

	HAPIdBrickType getBrickType();
	
	void processBrick(HAPPath pathFromRoot, HAPManualContextProcessBrick processContext);

	
	
	
	//supply custom constant value
	void extendConstantValue(HAPBlockComplex complexEntityExecutable, HAPManualContextProcessBrick processContext);
	
	//process definition before value context
	void processValueContext(HAPBlockComplex complexEntityExecutable, HAPManualContextProcessBrick processContext);
	void postProcessValueContext(HAPBlockComplex complexEntityExecutable, HAPManualContextProcessBrick processContext);

	//value context extension, variable resolve
	void processValueContextExtension(HAPBlockComplex complexEntityExecutable, HAPManualContextProcessBrick processContext);
	void postProcessValueContextExtension(HAPBlockComplex complexEntityExecutable, HAPManualContextProcessBrick processContext);
	
	//matcher
	void processValueContextDiscovery(HAPBlockComplex complexEntityExecutable, HAPManualContextProcessBrick processContext);
	void postProcessValueContextDiscovery(HAPBlockComplex complexEntityExecutable, HAPManualContextProcessBrick processContext);
	
	//process definition after value context
	void processEntity(HAPBlockComplex complexEntityExecutable, HAPManualContextProcessBrick processContext);
	void postProcessEntity(HAPBlockComplex complexEntityExecutable, HAPManualContextProcessBrick processContext);

}
