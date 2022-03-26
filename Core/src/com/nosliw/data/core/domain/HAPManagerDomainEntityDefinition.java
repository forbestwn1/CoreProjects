package com.nosliw.data.core.domain;

import java.util.LinkedHashMap;
import java.util.Map;

public class HAPManagerDomainEntityDefinition {

	private Map<String, HAPPluginEntityDefinitionInDomain> m_entityDefinitionPlugin;
	
	private Map<String, HAPPluginAdapterDefinition> m_adapterPlugin;
	private Map<String, String> m_defaultAdapter;
	
	public HAPManagerDomainEntityDefinition() {
		this.m_entityDefinitionPlugin = new LinkedHashMap<String, HAPPluginEntityDefinitionInDomain>();
		this.m_adapterPlugin = new LinkedHashMap<String, HAPPluginAdapterDefinition>();
		this.m_defaultAdapter = new LinkedHashMap<String, String>();
	}
	
	public HAPIdEntityInDomain parseDefinition(String entityType, Object obj, HAPContextParser parseContext) {
		if(obj==null)  return null;
		return this.m_entityDefinitionPlugin.get(entityType).parseDefinition(obj, parseContext);
	}
	
	public void registerEntityDefinitionPlugin(HAPPluginEntityDefinitionInDomain plugin) {		this.m_entityDefinitionPlugin.put(plugin.getEntityType(), plugin);	}

	public boolean isComplexEntity(String entityType) {     return this.m_entityDefinitionPlugin.get(entityType).isComplexEntity();   }
	
	public Object parseAdapter(String adapterType, Object obj) {		return this.m_adapterPlugin.get(adapterType).parseAdapter(obj);	}
	
	public String getDefaultAdapterByEntity(String entityType) {    return this.m_defaultAdapter.get(entityType);	}
	
}
