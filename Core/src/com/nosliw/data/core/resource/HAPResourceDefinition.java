package com.nosliw.data.core.resource;

import com.nosliw.common.info.HAPEntityInfo;
import com.nosliw.common.serialization.HAPSerializable;
import com.nosliw.data.core.component.HAPLocalReferenceBase;

public interface HAPResourceDefinition extends HAPResourceDefinitionOrId, HAPEntityInfo, HAPSerializable{

	void setResourceId(HAPResourceId resourceId);
	
	HAPResourceId getResourceId();
	
	String getResourceType();

	//get embeded resource definition
	HAPResourceDefinitionOrId getChild(String path);
	
	//path base for local resource reference
	HAPLocalReferenceBase getLocalReferenceBase();

	void setLocalReferenceBase(HAPLocalReferenceBase localRefBase);

	void cloneToResourceDefinition(HAPResourceDefinition resourceDef);

}
