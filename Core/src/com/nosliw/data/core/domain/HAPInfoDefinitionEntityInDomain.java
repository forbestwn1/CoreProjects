package com.nosliw.data.core.domain;

import java.util.Set;

import com.nosliw.data.core.component.HAPLocalReferenceBase;

public class HAPInfoDefinitionEntityInDomain {

	//for resource in a folder, the base path
	private HAPLocalReferenceBase m_basePath;

	private HAPDefinitionEntityInDomain m_entity;
	
	//alias for this entity
	private Set<String> m_alias;
	
	//for id from external system
	private String m_globalId;
	
	private HAPIdEntityInDomain m_entityId;

	public HAPDefinitionEntityInDomain getEntity() {     return this.m_entity;     }
	
	public HAPLocalReferenceBase getLocalBaseReference() {     return this.m_basePath;     }

	public boolean isComplexEntity() {   return false;   }
	
	public HAPIdEntityInDomain getEntityId() {    return this.m_entityId;     }

	public String getGlobalId() {    return this.m_globalId;    }
}
