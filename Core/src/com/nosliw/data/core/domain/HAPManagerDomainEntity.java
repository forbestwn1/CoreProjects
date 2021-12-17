package com.nosliw.data.core.domain;

import java.util.Map;

import org.json.JSONObject;

public class HAPManagerDomainEntity {

	private Map<String, HAPPluginEntityDefinitionInDomain> m_entityDefinitionPlugin;
	
	public HAPIdEntityInDomain parseDefinition(String entityType, JSONObject jsonObj, HAPContextParser parseContext) {
		return this.m_entityDefinitionPlugin.get(entityType).parseDefinition(jsonObj, parseContext);
	}
	
	public void registerEntityDefinitionPlugin(HAPPluginEntityDefinitionInDomain plugin) {
		this.m_entityDefinitionPlugin.put(plugin.getEntityType(), plugin);
	}
	
}
