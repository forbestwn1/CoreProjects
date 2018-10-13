package com.nosliw.data.core.service;

public interface HAPFactoryService {

	//create service instance
	HAPExecutableService newService(HAPDefinitionService serviceDefinition);
	
}
