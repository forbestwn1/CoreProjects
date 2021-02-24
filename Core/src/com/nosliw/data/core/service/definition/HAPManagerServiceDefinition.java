package com.nosliw.data.core.service.definition;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import com.nosliw.data.core.runtime.HAPRuntimeEnvironment;

//manage all service definition
public class HAPManagerServiceDefinition {

	private HAPRuntimeEnvironment m_runtimeEnv;
	
	private Map<String, HAPDefinitionService> m_definitions;
	
	public HAPManagerServiceDefinition(HAPRuntimeEnvironment runtimeEnv){
		this.m_runtimeEnv = runtimeEnv;
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
		HAPDefinitionService out = this.m_definitions.get(id);
		if(!out.isProcessed())   out.process(this.m_runtimeEnv);
		return out;
	}
	
	public List<HAPDefinitionService> queryDefinition(HAPQueryServiceDefinition query){
		List<HAPDefinitionService> out = new ArrayList<HAPDefinitionService>();
		for(String id : this.m_definitions.keySet()) {
			boolean found = true;
			HAPDefinitionService def = this.m_definitions.get(id);
			List<String> tags = def.getStaticInfo().getTags();
			for(String keyword : query.getKeywords()) {
				if(!tags.contains(keyword))  found = false;
			}
			if(found)  out.add(def);
		}
		return out;
	}
}
