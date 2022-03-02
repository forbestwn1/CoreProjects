package com.nosliw.data.core.domain;

import java.util.LinkedHashMap;
import java.util.Map;

public class HAPManagerDomainEntity {

	private Map<String, HAPPluginEntityDefinitionInDomain> m_entityDefinitionPlugin;
	
	public HAPManagerDomainEntity() {
		this.m_entityDefinitionPlugin = new LinkedHashMap<String, HAPPluginEntityDefinitionInDomain>();
	}
	
	public HAPIdEntityInDomain parseDefinition(String entityType, Object obj, HAPContextParser parseContext) {
		if(obj==null)  return null;
		return this.m_entityDefinitionPlugin.get(entityType).parseDefinition(obj, parseContext);
	}
	
	public void registerEntityDefinitionPlugin(HAPPluginEntityDefinitionInDomain plugin) {
		this.m_entityDefinitionPlugin.put(plugin.getEntityType(), plugin);
	}
	
}
