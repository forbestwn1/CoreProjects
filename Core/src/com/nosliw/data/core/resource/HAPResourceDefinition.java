package com.nosliw.data.core.resource;

import com.nosliw.data.core.common.HAPEntityOrReference;

public interface HAPResourceDefinition extends HAPEntityOrReference{

	void setResourceId(HAPResourceId resourceId);
	
	HAPResourceId getResourceId();
	
	String getResourceType();

	void cloneToResourceDefinition(HAPResourceDefinition resourceDef);

}
