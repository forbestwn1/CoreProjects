package com.nosliw.core.resource;

import java.util.Map;

public interface HAPProviderResourcePlugin {

	Map<HAPIdResourceType, HAPPluginResourceManager> getResourceManagerPlugins();
	
}
