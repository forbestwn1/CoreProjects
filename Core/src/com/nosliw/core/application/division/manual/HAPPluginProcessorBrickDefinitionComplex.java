package com.nosliw.core.application.division.manual;

import com.nosliw.common.path.HAPPath;
import com.nosliw.core.application.HAPBrickComplex;
import com.nosliw.core.application.HAPIdBrickType;

public interface HAPPluginProcessorBrickDefinitionComplex extends HAPPluginProcessorBrickDefinition{

	HAPIdBrickType getBrickType();
	
	//new executable
	HAPBrickComplex newExecutable();


	void processBrick(HAPPath pathFromRoot, HAPManualContextProcess processContext);

	
	
	
	//supply custom constant value
	void extendConstantValue(HAPBrickComplex complexEntityExecutable, HAPManualContextProcess processContext);
	
	//process definition before value context
	void processValueContext(HAPBrickComplex complexEntityExecutable, HAPManualContextProcess processContext);
	void postProcessValueContext(HAPBrickComplex complexEntityExecutable, HAPManualContextProcess processContext);

	//value context extension, variable resolve
	void processValueContextExtension(HAPBrickComplex complexEntityExecutable, HAPManualContextProcess processContext);
	void postProcessValueContextExtension(HAPBrickComplex complexEntityExecutable, HAPManualContextProcess processContext);
	
	//matcher
	void processValueContextDiscovery(HAPBrickComplex complexEntityExecutable, HAPManualContextProcess processContext);
	void postProcessValueContextDiscovery(HAPBrickComplex complexEntityExecutable, HAPManualContextProcess processContext);
	
	//process definition after value context
	void processEntity(HAPBrickComplex complexEntityExecutable, HAPManualContextProcess processContext);
	void postProcessEntity(HAPBrickComplex complexEntityExecutable, HAPManualContextProcess processContext);

}
