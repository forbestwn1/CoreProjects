package com.nosliw.data.core.entity.division.manual;

import com.nosliw.data.core.entity.HAPEntityExecutableComplex;

public interface HAPPluginProcessorEntityDefinitionComplex extends HAPPluginProcessorEntityDefinition{

	String getEntityType();
	
	//new executable
	HAPEntityExecutableComplex newExecutable();

	//supply custom constant value
	void extendConstantValue(HAPEntityExecutableComplex complexEntityExecutable, HAPContextProcess processContext);
	
	//process definition before value context
	void processValueContext(HAPEntityExecutableComplex complexEntityExecutable, HAPContextProcess processContext);
	void postProcessValueContext(HAPEntityExecutableComplex complexEntityExecutable, HAPContextProcess processContext);

	//value context extension, variable resolve
	void processValueContextExtension(HAPEntityExecutableComplex complexEntityExecutable, HAPContextProcess processContext);
	void postProcessValueContextExtension(HAPEntityExecutableComplex complexEntityExecutable, HAPContextProcess processContext);
	
	//matcher
	void processValueContextDiscovery(HAPEntityExecutableComplex complexEntityExecutable, HAPContextProcess processContext);
	void postProcessValueContextDiscovery(HAPEntityExecutableComplex complexEntityExecutable, HAPContextProcess processContext);
	
	//process definition after value context
	void processEntity(HAPEntityExecutableComplex complexEntityExecutable, HAPContextProcess processContext);
	void postProcessEntity(HAPEntityExecutableComplex complexEntityExecutable, HAPContextProcess processContext);

}
