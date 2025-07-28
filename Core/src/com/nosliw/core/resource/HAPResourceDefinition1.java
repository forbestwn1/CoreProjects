package com.nosliw.core.resource;

import com.nosliw.common.info.HAPEntityInfo;
import com.nosliw.common.serialization.HAPSerializable;
import com.nosliw.data.core.component.HAPPathLocationBase;
import com.nosliw.data.core.domain.HAPIdEntityInDomain;

public interface HAPResourceDefinition1 extends HAPResourceDefinitionOrId, HAPEntityInfo, HAPSerializable{

	HAPResourceId getResourceId();

	//type of resource
	String getResourceType();

	//entity id in domain
	HAPIdEntityInDomain getEntityId();
	void setEntityId(HAPIdEntityInDomain entityId);
	
	//path base for local resource reference
	HAPPathLocationBase getLocalReferenceBase();
	void setLocalReferenceBase(HAPPathLocationBase localRefBase);

	void cloneToResourceDefinition(HAPResourceDefinition1 resourceDef);

	//get embeded resource definition
	HAPResourceDefinitionOrId getChild(String path);


}
