package com.nosliw.data.core.component;

import com.nosliw.data.core.resource.HAPResourceDefinition;
import com.nosliw.data.core.resource.HAPResourceIdSimple;

public interface HAPPluginResourceDefinition {

	String getResourceType();
	
	HAPResourceDefinition getResource(HAPResourceIdSimple resourceId);
	
}
