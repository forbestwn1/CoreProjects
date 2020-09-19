package com.nosliw.data.core.resource;

import com.nosliw.common.interfac.HAPEntityOrReference;
import com.nosliw.common.serialization.HAPSerializable;

public interface HAPResourceDefinition extends HAPEntityOrReference, HAPSerializable{

	void setResourceId(HAPResourceId resourceId);
	
	HAPResourceId getResourceId();
	
	String getResourceType();

	void cloneToResourceDefinition(HAPResourceDefinition resourceDef);

}
