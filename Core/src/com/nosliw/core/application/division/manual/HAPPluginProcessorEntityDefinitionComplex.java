package com.nosliw.core.application.division.manual;

import com.nosliw.common.path.HAPPath;
import com.nosliw.core.application.HAPBrickComplex;

public interface HAPPluginProcessorEntityDefinitionComplex extends HAPPluginProcessorEntityDefinition{

	String getEntityType();
	
	//new executable
	HAPBrickComplex newExecutable();


	void processEntity(HAPPath pathFromRoot, HAPContextProcess processContext);

	
	
	
	//supply custom constant value
	void extendConstantValue(HAPBrickComplex complexEntityExecutable, HAPContextProcess processContext);
	
	//process definition before value context
	void processValueContext(HAPBrickComplex complexEntityExecutable, HAPContextProcess processContext);
	void postProcessValueContext(HAPBrickComplex complexEntityExecutable, HAPContextProcess processContext);

	//value context extension, variable resolve
	void processValueContextExtension(HAPBrickComplex complexEntityExecutable, HAPContextProcess processContext);
	void postProcessValueContextExtension(HAPBrickComplex complexEntityExecutable, HAPContextProcess processContext);
	
	//matcher
	void processValueContextDiscovery(HAPBrickComplex complexEntityExecutable, HAPContextProcess processContext);
	void postProcessValueContextDiscovery(HAPBrickComplex complexEntityExecutable, HAPContextProcess processContext);
	
	//process definition after value context
	void processEntity(HAPBrickComplex complexEntityExecutable, HAPContextProcess processContext);
	void postProcessEntity(HAPBrickComplex complexEntityExecutable, HAPContextProcess processContext);

}
