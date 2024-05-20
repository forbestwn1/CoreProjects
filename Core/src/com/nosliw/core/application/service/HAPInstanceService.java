package com.nosliw.core.application.service;

import com.nosliw.core.application.brick.service.profile.HAPBlockServiceProfile;

//service instance
public class HAPInstanceService {

	//object that contain all information related with this service
	private HAPBlockServiceProfile m_definition;
	
	//object that answer the service call  
	private HAPExecutableService m_executable;
	
	public HAPInstanceService(HAPBlockServiceProfile definition, HAPExecutableService executable) {
		this.m_definition = definition;
		this.m_executable = executable;
	}
	
	public HAPBlockServiceProfile getDefinition() {		return this.m_definition;	}
	
	public HAPExecutableService getExecutable() {   return this.m_executable;    }
	
}
