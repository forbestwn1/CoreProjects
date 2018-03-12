package com.nosliw.data.core.datasource;

import java.util.LinkedHashMap;
import java.util.Map;

public class HAPDataSourceDefinitionManager {

	private Map<String, HAPDefinition> m_definitions;
	
	public HAPDataSourceDefinitionManager(){
		this.m_definitions = new LinkedHashMap<String, HAPDefinition>();
	}
	
	public void registerDataSourceDefinition(String name, HAPDefinition dataSourceDefinition){
		this.m_definitions.put(name, dataSourceDefinition);
	}
	
	public HAPDefinition getDataSourceDefinition(String name){
		return this.m_definitions.get(name);
	}
	
}
