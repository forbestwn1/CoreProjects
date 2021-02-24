package com.nosliw.data.core.service.definition;

//service instance
public class HAPInstanceService {

	//object that contain all information related with this service
	private HAPDefinitionService m_definition;
	
	//object that answer the service call  
	private HAPExecutableService m_executable;
	
	public HAPInstanceService(HAPDefinitionService definition, HAPExecutableService executable) {
		this.m_definition = definition;
		this.m_executable = executable;
	}
	
	public HAPDefinitionService getDefinition() {		return this.m_definition;	}
	
	public HAPExecutableService getExecutable() {   return this.m_executable;    }
	
}
