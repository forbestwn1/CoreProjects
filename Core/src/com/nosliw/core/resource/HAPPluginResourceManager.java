package com.nosliw.core.resource;

import com.nosliw.core.runtime.HAPRuntimeInfo;

public interface HAPPluginResourceManager {

	HAPResourceDataOrWrapper getResourceData(HAPResourceIdSimple simpleResourceId, HAPRuntimeInfo runtimeInfo);
	
}
 