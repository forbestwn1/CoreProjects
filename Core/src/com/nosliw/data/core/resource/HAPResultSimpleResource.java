package com.nosliw.data.core.resource;

import com.nosliw.data.core.complex.HAPIdEntityInDomain;
import com.nosliw.data.core.component.HAPLocalReferenceBase;

public class HAPResultSimpleResource {

	//for resource in a folder, the base path
	private HAPLocalReferenceBase m_basePath;

	private HAPIdEntityInDomain m_entityId;
	
	public HAPResultSimpleResource(HAPIdEntityInDomain entityId, HAPLocalReferenceBase basePath) {
		this.m_entityId = entityId;
		this.m_basePath = basePath;
	}
	
	public HAPIdEntityInDomain getEntityId() {    return this.m_entityId;    }
	
	public HAPLocalReferenceBase getLocalReferenceBase() {    return this.m_basePath;     }
	
}
