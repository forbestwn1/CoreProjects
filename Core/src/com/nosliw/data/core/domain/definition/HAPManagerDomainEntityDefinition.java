package com.nosliw.data.core.domain.definition;

import java.util.LinkedHashMap;
import java.util.Map;

import com.nosliw.common.serialization.HAPSerializationFormat;
import com.nosliw.data.core.domain.HAPContextParser;
import com.nosliw.data.core.domain.HAPIdEntityInDomain;
import com.nosliw.data.core.entity.division.manual.HAPManualEntity;
import com.nosliw.data.core.resource.HAPResourceIdSimple;

public class HAPManagerDomainEntityDefinition {

	private Map<String, HAPPluginEntityDefinition> m_entityDefinitionPlugin;
	
	private Map<String, HAPPluginResourceDefinition> m_resourceDefinitionPlugin;
	
	private Map<String, HAPPluginAdapterDefinition> m_adapterPlugin;
	private Map<String, String> m_defaultAdapter;
	
	public HAPManagerDomainEntityDefinition() {
		this.m_entityDefinitionPlugin = new LinkedHashMap<String, HAPPluginEntityDefinition>();
		this.m_adapterPlugin = new LinkedHashMap<String, HAPPluginAdapterDefinition>();
		this.m_defaultAdapter = new LinkedHashMap<String, String>();
	}
	
	public HAPManualEntity getEntityDefinition(HAPResourceIdSimple resourceId) {
		
	}
	
	
	
	public HAPManualEntity parseDefinition(String entityType, Object obj, HAPSerializationFormat format) {
		if(obj==null) {
			return null;
		}
		
		HAPPluginEntityDefinition plugin = this.m_entityDefinitionPlugin.get(entityType);
		return plugin.parse(obj, format);		
	}
	

	
	
	
	
	
	
	public HAPIdEntityInDomain newDefinitionInstance(String entityType, HAPContextParser parseContext) {
		HAPPluginEntityDefinition plugin = this.m_entityDefinitionPlugin.get(entityType);
		return plugin.newInstance(parseContext);
	}
	
	public HAPIdEntityInDomain parseDefinition(String entityType, Object obj, HAPSerializationFormat format, HAPContextParser parseContext) {
		if(obj==null) {
			return null;
		}
		
		HAPPluginEntityDefinition plugin = this.m_entityDefinitionPlugin.get(entityType);
		HAPIdEntityInDomain out = plugin.newInstance(parseContext);
		plugin.parseDefinition(out, obj, format, parseContext);		
		return out;
	}
	
	public void registerEntityDefinitionPlugin(HAPPluginEntityDefinition plugin) {		this.m_entityDefinitionPlugin.put(plugin.getEntityType(), plugin);	}

	public boolean isComplexEntity(String entityType) {     return this.m_entityDefinitionPlugin.get(entityType).isComplexEntity();   }
	
	public Object parseAdapter(String adapterType, Object obj) {		return this.m_adapterPlugin.get(adapterType).parseAdapter(obj);	}
	
	public String getDefaultAdapterByEntity(String entityType) {    return this.m_defaultAdapter.get(entityType);	}
	
}
