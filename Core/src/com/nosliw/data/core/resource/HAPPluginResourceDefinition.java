package com.nosliw.data.core.resource;

public interface HAPPluginResourceDefinition {

	String getResourceType();
	
	HAPResourceDefinition getResourceDefinitionBySimpleResourceId(HAPResourceIdSimple resourceId);
	
	HAPResourceDefinition getResourceDefinitionByLocalResourceId(HAPResourceIdLocal resourceId);
	
	HAPResourceDefinition parseResourceDefinition(Object content);

	
}
