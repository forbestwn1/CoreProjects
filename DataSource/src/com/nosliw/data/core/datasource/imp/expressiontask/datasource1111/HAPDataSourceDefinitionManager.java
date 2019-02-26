package com.nosliw.data.core.datasource;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class HAPDataSourceDefinitionManager {

	private Map<String, HAPDefinition> m_definitions;
	
	public HAPDataSourceDefinitionManager(){
		this.m_definitions = new LinkedHashMap<String, HAPDefinition>();
		List<HAPDefinition> defs = HAPDataSourceDefinitionImporter.loadDataSourceDefinition();
		for(HAPDefinition def : defs) {
			this.registerDataSourceDefinition(def);
		}
	}
	
	public void registerDataSourceDefinition(HAPDefinition dataSourceDefinition){
		this.m_definitions.put(dataSourceDefinition.getName(), dataSourceDefinition);
	}
	
	public HAPDefinition getDataSourceDefinition(String name){
		return this.m_definitions.get(name);
	}
	
}
