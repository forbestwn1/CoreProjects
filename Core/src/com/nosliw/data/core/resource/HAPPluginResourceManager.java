package com.nosliw.data.core.resource;

import com.nosliw.data.core.runtime.HAPRuntimeInfo;

public interface HAPPluginResourceManager {

	HAPResourceDataOrWrapper getResourceData(HAPResourceIdSimple simpleResourceId, HAPRuntimeInfo runtimeInfo);
	
}
 