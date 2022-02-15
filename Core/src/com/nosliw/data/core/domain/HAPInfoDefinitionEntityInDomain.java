package com.nosliw.data.core.domain;

import java.util.HashSet;
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

	public HAPInfoDefinitionEntityInDomain() {
		this.m_alias = new HashSet<String>();
	}
	
	public HAPDefinitionEntityInDomain getEntity() {     return this.m_entity;     }
	public void setEntity(HAPDefinitionEntityInDomain entityDef) {   this.m_entity = entityDef;    }
	
	public HAPLocalReferenceBase getLocalBaseReference() {     return this.m_basePath;     }
	public void setLocalBaseReference(HAPLocalReferenceBase basePath) {     this.m_basePath = basePath;    }

	public HAPIdEntityInDomain getEntityId() {    return this.m_entityId;     }
	public void setEntityId(HAPIdEntityInDomain entityId) {    this.m_entityId = entityId;    }

	public String getGlobalId() {    return this.m_globalId;    }
	public void setGlobalId(String globalId) {  this.m_globalId = globalId;   }
	
	public Set<String> getAlias(){    return this.m_alias;    }
	
	public boolean isComplexEntity() {   return false;   }
	
	public void cloneToInfoDefinitionEntityInDomain(HAPInfoDefinitionEntityInDomain out) {
		out.m_alias.addAll(this.m_alias);
		if(this.m_basePath!=null)  out.m_basePath = this.m_basePath.cloneLocalReferenceBase();
		if(this.m_entity!=null)  out.m_entity = this.m_entity.cloneEntityDefinitionInDomain();
		if(this.m_entityId!=null)   out.m_entityId = this.m_entityId.cloneIdEntityInDomain();
	}
	
	public HAPInfoDefinitionEntityInDomain cloneEntityDefinitionInfo() {
		HAPInfoDefinitionEntityInDomain out = new HAPInfoDefinitionEntityInDomain();
		this.cloneToInfoDefinitionEntityInDomain(out);
		return out;
	}
}
