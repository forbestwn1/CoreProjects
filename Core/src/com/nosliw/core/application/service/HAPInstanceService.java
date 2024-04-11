package com.nosliw.core.application.service;

import com.nosliw.core.application.brick.service.profile.HAPBrickServiceProfile;

//service instance
public class HAPInstanceService {

	//object that contain all information related with this service
	private HAPBrickServiceProfile m_definition;
	
	//object that answer the service call  
	private HAPExecutableService m_executable;
	
	public HAPInstanceService(HAPBrickServiceProfile definition, HAPExecutableService executable) {
		this.m_definition = definition;
		this.m_executable = executable;
	}
	
	public HAPBrickServiceProfile getDefinition() {		return this.m_definition;	}
	
	public HAPExecutableService getExecutable() {   return this.m_executable;    }
	
}
