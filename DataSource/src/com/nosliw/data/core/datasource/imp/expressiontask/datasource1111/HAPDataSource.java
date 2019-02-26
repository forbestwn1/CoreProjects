package com.nosliw.data.core.datasource;

public class HAPDataSource {

	private HAPDefinition m_definition;
	
	private HAPExecutableDataSource m_executable;
	
	public HAPDataSource(HAPDefinition definition, HAPExecutableDataSource executable) {
		this.m_definition = definition;
		this.m_executable = executable;
	}
	
	
	public HAPDefinition getDefinition() {		return this.m_definition;	}
	
	public HAPExecutableDataSource getExecutable() {   return this.m_executable;    }
	
}
