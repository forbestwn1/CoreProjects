package com.nosliw.data.core.service;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

//manage all service definition
public class HAPManagerServiceDefinition {

	private Map<String, HAPDefinitionService> m_definitions;
	
	public HAPManagerServiceDefinition(){
		this.m_definitions = new LinkedHashMap<String, HAPDefinitionService>();
		List<HAPDefinitionService> defs = HAPImporterDataSourceDefinition.loadDataSourceDefinition();
		for(HAPDefinitionService def : defs) {
			this.registerDefinition(def);
		}
	}
	
	public void registerDefinition(HAPDefinitionService serviceDefinition){
		this.m_definitions.put(serviceDefinition.getStaticInfo().getId(), serviceDefinition);
	}
	
	public HAPDefinitionService getDefinition(String id){
		return this.m_definitions.get(id);
	}
	
}
