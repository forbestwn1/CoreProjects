package com.nosliw.core.resource.imp.js.library;

import java.util.LinkedHashMap;
import java.util.Map;

import org.springframework.stereotype.Component;

import com.nosliw.common.utils.HAPConstantShared;
import com.nosliw.core.resource.HAPFactoryResourceTypeId;
import com.nosliw.core.resource.HAPIdResourceType;
import com.nosliw.core.resource.HAPPluginResourceManager;
import com.nosliw.core.resource.HAPProviderResourcePlugin;

@Component
public class HAPProviderResourcePluginJSLibrary  implements HAPProviderResourcePlugin{

	Map<HAPIdResourceType, HAPPluginResourceManager> m_plugins = new LinkedHashMap<HAPIdResourceType, HAPPluginResourceManager>();
	
	public HAPProviderResourcePluginJSLibrary() {
		m_plugins.put(HAPFactoryResourceTypeId.newInstance(HAPConstantShared.RUNTIME_RESOURCE_TYPE_JSLIBRARY), new HAPPluginResourceManagerJSLibrary());
	}
	
	@Override
	public Map<HAPIdResourceType, HAPPluginResourceManager> getResourceManagerPlugins() {
		return m_plugins;
	}
}
