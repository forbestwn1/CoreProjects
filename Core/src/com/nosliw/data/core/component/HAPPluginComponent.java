package com.nosliw.data.core.component;

import com.nosliw.data.core.resource.HAPResourceIdSimple;

public interface HAPPluginComponent {

	String getComponentType();
	
	HAPComponent getComponent(HAPResourceIdSimple resourceId);
	
}
