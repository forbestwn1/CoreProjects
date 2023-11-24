package com.nosliw.data.core.domain.entity;

import com.nosliw.data.core.component.HAPContextProcessor;

public interface HAPPluginEntityProcessorComplex {

	String getEntityType();
	
	//new executable
	HAPExecutableEntityComplex newExecutable();

	//supply custom constant value
	void extendConstantValue(HAPExecutableEntityComplex complexEntityExecutable, HAPContextProcessor processContext);
	
	//process definition before value context
	void processValueContext(HAPExecutableEntityComplex complexEntityExecutable, HAPContextProcessor processContext);
	void postProcessValueContext(HAPExecutableEntityComplex complexEntityExecutable, HAPContextProcessor processContext);

	//value context extension, variable resolve
	void processValueContextExtension(HAPExecutableEntityComplex complexEntityExecutable, HAPContextProcessor processContext);
	void postProcessValueContextExtension(HAPExecutableEntityComplex complexEntityExecutable, HAPContextProcessor processContext);
	
	//matcher
	void processValueContextDiscovery(HAPExecutableEntityComplex complexEntityExecutable, HAPContextProcessor processContext);
	void postProcessValueContextDiscovery(HAPExecutableEntityComplex complexEntityExecutable, HAPContextProcessor processContext);
	
	//process definition after value context
	void processEntity(HAPExecutableEntityComplex complexEntityExecutable, HAPContextProcessor processContext);
	void postProcessEntity(HAPExecutableEntityComplex complexEntityExecutable, HAPContextProcessor processContext);

}
