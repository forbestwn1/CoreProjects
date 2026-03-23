package com.nosliw.core.resource;

import java.util.LinkedHashMap;
import java.util.Map;

public abstract class HAPProviderResourcePluginImp implements HAPProviderResourcePlugin{

	private Map<HAPIdResourceType, HAPPluginResourceManager> m_plugins = new LinkedHashMap<HAPIdResourceType, HAPPluginResourceManager>();
	
	protected void registerPlugin(HAPIdResourceType resourceTypeId, HAPPluginResourceManager resourceManagerPlugin) {
		this.m_plugins.put(resourceTypeId, resourceManagerPlugin);
	}
	
	@Override
	public Map<HAPIdResourceType, HAPPluginResourceManager> getResourceManagerPlugins() {
		return m_plugins;
	}
	
}
