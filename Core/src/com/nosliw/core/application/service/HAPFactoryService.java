package com.nosliw.core.application.service;

import com.nosliw.core.application.brick.service.profile.HAPBlockServiceProfile;

//service instance factory that generate service instance according to service definition
public interface HAPFactoryService {

	//create service instance
	HAPExecutableService newService(HAPBlockServiceProfile serviceDefinition);
	
}
