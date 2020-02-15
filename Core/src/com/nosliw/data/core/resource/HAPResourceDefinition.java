package com.nosliw.data.core.resource;

public interface HAPResourceDefinition extends HAPResourceDefinitionOrReference{

	void setResourceId(HAPResourceId resourceId);
	
	HAPResourceId getResourceId();
	
	String getResourceType();
}
