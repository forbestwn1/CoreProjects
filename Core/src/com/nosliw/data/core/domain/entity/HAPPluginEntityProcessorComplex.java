package com.nosliw.data.core.domain.entity;

import com.nosliw.data.core.component.HAPContextProcessor;
import com.nosliw.data.core.domain.HAPIdEntityInDomain;

public interface HAPPluginEntityProcessorComplex {

	String getEntityType();
	
	//new executable
	HAPExecutableEntityComplex newExecutable();

	//process definition before value context
	void init(HAPIdEntityInDomain complexEntityExecutableId, HAPContextProcessor processContext);
	void postInit(HAPIdEntityInDomain complexEntityExecutableId, HAPContextProcessor processContext);

	//value context extension, variable resolve
	void processValueContextExtension(HAPIdEntityInDomain complexEntityExecutableId, HAPContextProcessor processContext);
	void postProcessValueContextExtension(HAPIdEntityInDomain complexEntityExecutableId, HAPContextProcessor processContext);
	
	//matcher
	void processValueContextDiscovery(HAPIdEntityInDomain complexEntityExecutableId, HAPContextProcessor processContext);
	void postProcessValueContextDiscovery(HAPIdEntityInDomain complexEntityExecutableId, HAPContextProcessor processContext);
	
	//process definition after value context
	void process(HAPIdEntityInDomain complexEntityExecutableId, HAPContextProcessor processContext);
	void postProcess(HAPIdEntityInDomain complexEntityExecutableId, HAPContextProcessor processContext);

}
