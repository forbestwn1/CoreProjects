package com.nosliw.data.core.resource;

import com.nosliw.common.info.HAPEntityInfo;
import com.nosliw.common.serialization.HAPSerializable;

public interface HAPResourceDefinition extends HAPResourceDefinitionOrId, HAPEntityInfo, HAPSerializable{

	void setResourceId(HAPResourceId resourceId);
	
	HAPResourceId getResourceId();
	
	String getResourceType();

	//get embeded resource definition
	HAPResourceDefinitionOrId getChild(String path);
	
	void cloneToResourceDefinition(HAPResourceDefinition resourceDef);

}
