package com.nosliw.data.core.domain;

import com.nosliw.common.serialization.HAPSerializableImp;
import com.nosliw.data.core.component.HAPPathLocationBase;

public class HAPInfoDefinitionEntityInDomain extends HAPSerializableImp{

	//entity definition
	public static final String ENTITY = "entity";
	
	//extra info definition
	public static final String EXTRA = "extra";
	
	//parent info definition
	public static final String PARENT = "parent";
	
	//location path base info
	private HAPPathLocationBase m_baseLocationPath;

	//entity object
	private HAPDefinitionEntityInDomain m_entity;
	
	//entity id
	private HAPIdEntityInDomain m_entityId;

	//extra info provided by user (name, description, alias, global id, info, ...)
	private HAPInfoDefinitionEntityInDomainExtra m_extraInfo;
	
	public HAPInfoDefinitionEntityInDomain() {
		this.m_extraInfo = new HAPInfoDefinitionEntityInDomainExtra();
	}
	
	public HAPDefinitionEntityInDomain getEntity() {     return this.m_entity;     }
	public void setEntity(HAPDefinitionEntityInDomain entityDef) {   this.m_entity = entityDef;    }
	
	public HAPPathLocationBase getBaseLocationPath() {     return this.m_baseLocationPath;     }
	public void setBaseLocationPath(HAPPathLocationBase baseLocationPath) {     this.m_baseLocationPath = baseLocationPath;    }

	public HAPIdEntityInDomain getEntityId() {    return this.m_entityId;     }
	public void setEntityId(HAPIdEntityInDomain entityId) {    this.m_entityId = entityId;    }

	public HAPInfoDefinitionEntityInDomainExtra getExtraInfo() {   return this.m_extraInfo;      }
	
	public boolean isComplexEntity() {   return false;   }
	
	public void cloneToInfoDefinitionEntityInDomain(HAPInfoDefinitionEntityInDomain out) {
		if(this.m_baseLocationPath!=null)  out.m_baseLocationPath = this.m_baseLocationPath.cloneLocalReferenceBase();
		if(this.m_entity!=null)  out.m_entity = this.m_entity.cloneEntityDefinitionInDomain();
		if(this.m_entityId!=null)   out.m_entityId = this.m_entityId.cloneIdEntityInDomain();
	}
	
	public HAPInfoDefinitionEntityInDomain cloneEntityDefinitionInfo() {
		HAPInfoDefinitionEntityInDomain out = new HAPInfoDefinitionEntityInDomain();
		this.cloneToInfoDefinitionEntityInDomain(out);
		return out;
	}
}
