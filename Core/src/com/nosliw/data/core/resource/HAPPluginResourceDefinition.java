package com.nosliw.data.core.resource;

public interface HAPPluginResourceDefinition {

	String getResourceType();
	
	HAPResourceDefinition getResource(HAPResourceIdSimple resourceId);
	
	HAPResourceDefinition parseResourceDefinition(Object content);

}
