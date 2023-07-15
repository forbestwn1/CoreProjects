package com.nosliw.data.core.domain.entity;

import com.nosliw.data.core.component.HAPContextProcessor;
import com.nosliw.data.core.domain.HAPIdEntityInDomain;

public interface HAPPluginEntityProcessorComplex {

	String getEntityType();
	
	//process definition before value context
	void preProcess(HAPIdEntityInDomain complexEntityExecutableId, HAPContextProcessor processContext);

	//value context extension, variable resolve
	void processValueContextExtension(HAPIdEntityInDomain complexEntityExecutableId, HAPContextProcessor processContext);
	
	//matcher
	void processValueContextDiscovery(HAPIdEntityInDomain complexEntityExecutableId, HAPContextProcessor processContext);
	
	//process definition after value context
	void postProcess(HAPIdEntityInDomain complexEntityExecutableId, HAPContextProcessor processContext);

	//new executable
	HAPExecutableEntityComplex newExecutable();
	
}
