package com.nosliw.data.core.service.definition;

//service instance factory that generate service instance according to service definition
public interface HAPFactoryService {

	//create service instance
	HAPExecutableService newService(HAPDefinitionService serviceDefinition);
	
}
