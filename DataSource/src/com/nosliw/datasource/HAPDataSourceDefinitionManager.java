package com.nosliw.datasource;

import java.util.LinkedHashMap;
import java.util.Map;

public class HAPDataSourceDefinitionManager {

	private Map<String, HAPDataSourceDefinition> m_definitions;
	
	public HAPDataSourceDefinitionManager(){
		this.m_definitions = new LinkedHashMap<String, HAPDataSourceDefinition>();
	}
	
	public void registerDataSourceDefinition(String name, HAPDataSourceDefinition dataSourceDefinition){
		this.m_definitions.put(name, dataSourceDefinition);
	}
	
	public HAPDataSourceDefinition getDataSourceDefinition(String name){
		return this.m_definitions.get(name);
	}
	
}
