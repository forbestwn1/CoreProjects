package com.nosliw.core.xxx.resource;

import com.nosliw.data.core.component.HAPPathLocationBase;
import com.nosliw.data.core.domain.HAPIdEntityInDomain;

public class HAPResultSimpleResource {

	//for resource in a folder, the base path
	private HAPPathLocationBase m_basePath;

	private HAPIdEntityInDomain m_entityId;
	
	public HAPResultSimpleResource(HAPIdEntityInDomain entityId, HAPPathLocationBase basePath) {
		this.m_entityId = entityId;
		this.m_basePath = basePath;
	}
	
	public HAPIdEntityInDomain getEntityId() {    return this.m_entityId;    }
	
	public HAPPathLocationBase getLocalReferenceBase() {    return this.m_basePath;     }
	
}
