package com.nosliw.core.application.service;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import com.nosliw.core.application.brick.service.profile.HAPBrickServiceProfile;
import com.nosliw.data.core.runtime.HAPRuntimeEnvironment;

//manage all service definition
public class HAPManagerServiceDefinition {

	private HAPRuntimeEnvironment m_runtimeEnv;
	
	private Map<String, HAPBrickServiceProfile> m_definitions;
	
	public HAPManagerServiceDefinition(HAPRuntimeEnvironment runtimeEnv){
		this.m_runtimeEnv = runtimeEnv;
	}
	
	public void registerDefinition(HAPBrickServiceProfile serviceDefinition){
		this.getAllDefinitions().put(serviceDefinition.getStaticInfo().getId(), serviceDefinition);
	}
	
	public HAPBrickServiceProfile getDefinition(String id){
		HAPBrickServiceProfile out = this.getAllDefinitions().get(id);
		if(!out.isProcessed())   out.process(this.m_runtimeEnv);
		return out;
	}
	
	public List<HAPBrickServiceProfile> queryDefinition(HAPQueryServiceDefinition query){
		List<HAPBrickServiceProfile> out = new ArrayList<HAPBrickServiceProfile>();
		for(String id : this.getAllDefinitions().keySet()) {
			boolean found = true;
			HAPBrickServiceProfile def = this.getAllDefinitions().get(id);
			List<String> tags = def.getStaticInfo().getTags();
			for(String keyword : query.getKeywords()) {
				if(!tags.contains(keyword))  found = false;
			}
			if(found)  out.add(def);
		}
		return out;
	}
	
	private Map<String, HAPBrickServiceProfile> getAllDefinitions(){
		if(this.m_definitions==null) {
			this.m_definitions = new LinkedHashMap<String, HAPBrickServiceProfile>();
			List<HAPBrickServiceProfile> defs = HAPImporterDataSourceDefinition.loadDataSourceDefinition();
			for(HAPBrickServiceProfile def : defs) {
				this.registerDefinition(def);
			}
		}
		return this.m_definitions;
	}
}
