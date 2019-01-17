package com.nosliw.data.core.service;

//service instance
public class HAPInstanceService {

	private HAPDefinitionService m_definition;
	
	private HAPExecutableService m_executable;
	
	public HAPInstanceService(HAPDefinitionService definition, HAPExecutableService executable) {
		this.m_definition = definition;
		this.m_executable = executable;
	}
	
	public HAPDefinitionService getDefinition() {		return this.m_definition;	}
	
	public HAPExecutableService getExecutable() {   return this.m_executable;    }
	
}
