package com.nosliw.data.core.service;

public interface HAPFactoryService {

	HAPExecutableService newService(HAPDefinitionService serviceDefinition);
	
}
