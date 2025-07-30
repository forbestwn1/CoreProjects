package com.nosliw.core.resource;

import java.util.Map;

public interface HAPProviderResourcePlugin {

	public Map<HAPIdResourceType, HAPPluginResourceManager> getResourceManagerPlugins();
	
}
