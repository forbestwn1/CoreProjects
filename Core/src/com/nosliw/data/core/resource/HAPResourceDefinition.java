package com.nosliw.data.core.resource;

import com.nosliw.common.serialization.HAPSerializable;
import com.nosliw.data.core.common.HAPEntityOrReference;

public interface HAPResourceDefinition extends HAPEntityOrReference, HAPSerializable{

	void setResourceId(HAPResourceId resourceId);
	
	HAPResourceId getResourceId();
	
	String getResourceType();

	void cloneToResourceDefinition(HAPResourceDefinition resourceDef);

}
