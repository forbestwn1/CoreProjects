package com.nosliw.data.core.service.provide;

public interface HAPFactoryService {

	//create service instance
	HAPExecutableService newService(HAPDefinitionService serviceDefinition);
	
}
