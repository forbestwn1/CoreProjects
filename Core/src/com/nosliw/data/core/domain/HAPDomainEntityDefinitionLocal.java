package com.nosliw.data.core.domain;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import com.nosliw.common.interfac.HAPEntityOrReference;
import com.nosliw.common.serialization.HAPJsonUtility;
import com.nosliw.common.serialization.HAPSerializableImp;
import com.nosliw.common.serialization.HAPSerializationFormat;
import com.nosliw.common.utils.HAPConstantShared;
import com.nosliw.common.utils.HAPGeneratorId;
import com.nosliw.data.core.component.HAPPathLocationBase;
import com.nosliw.data.core.domain.entity.attachment.HAPReferenceAttachment;
import com.nosliw.data.core.resource.HAPResourceDefinition;
import com.nosliw.data.core.resource.HAPResourceId;
import com.nosliw.data.core.resource.HAPResourceIdLocal;
import com.nosliw.data.core.resource.HAPResourceIdSimple;

/*
 * domain resource represent a collection of entity for a simple resource 
 * it contains : 
 * 		embeded entity
 * 		entity of local resource
 */
public class HAPDomainEntityDefinitionLocal extends HAPSerializableImp implements HAPDomainEntity{
	
	public static String ENTITY = "entity";
	
	//domain id for this domain
	private String m_domainId;
	
	//simple resource id this domain represent
	private HAPResourceIdSimple m_resourceId;
	
	//location base info for resource
	private HAPPathLocationBase m_localRefBase;
	
	//entity that responding to root resource id
	private HAPIdEntityInDomain m_rootEntityId;
	
	//all entity
	private Map<HAPIdEntityInDomain, HAPInfoEntityInDomainDefinition> m_entity;

	//domain entity by local resource id 
	private Map<HAPResourceIdLocal, HAPResourceDefinition> m_entityIdByLocalResourceId;
	
	//id generator
	private HAPGeneratorId m_idGenerator;

	private HAPManagerDomainEntityDefinition m_entityDefMan;
	
	public HAPDomainEntityDefinitionLocal(HAPResourceIdSimple resourceId, HAPGeneratorId idGenerator, HAPManagerDomainEntityDefinition entityDefMan) {
		this.m_resourceId = resourceId;
		this.m_domainId = this.m_resourceId.toStringValue(HAPSerializationFormat.LITERATE);
		this.m_idGenerator = idGenerator;
		this.m_entityDefMan = entityDefMan;
		this.m_entity = new LinkedHashMap<HAPIdEntityInDomain, HAPInfoEntityInDomainDefinition>();
		this.m_entityIdByLocalResourceId = new LinkedHashMap<HAPResourceIdLocal, HAPResourceDefinition>();
	}

	public boolean isForComplexEntity() {  return false;    }
	
	public String getDomainId() {    return this.m_domainId;     }
	
	public HAPPathLocationBase getLocationBase() {    return this.m_localRefBase;    }
	public void setLocationBase(HAPPathLocationBase locationBase) {     this.m_localRefBase = locationBase;     }
	
	public HAPIdEntityInDomain getRootEntityId() {    return this.m_rootEntityId;     }
	public void setRootEntityId(HAPIdEntityInDomain entityId) {   this.m_rootEntityId = entityId;      }
	
	@Override
	public HAPInfoEntityInDomain getEntityInfo(HAPIdEntityInDomain entityId) {    return this.getEntityInfoDefinition(entityId);     }
	public HAPInfoEntityInDomainDefinition getEntityInfoDefinition(HAPIdEntityInDomain entityId) {		return this.m_entity.get(entityId); 	}
	
	public void addLocalResourceDefinition(HAPResourceDefinition resourceDef) {	this.m_entityIdByLocalResourceId.put((HAPResourceIdLocal)resourceDef.getResourceId(), resourceDef);	}
	public HAPResourceDefinition getLocalResourceDefinition(HAPResourceIdLocal resourceId) {    return this.m_entityIdByLocalResourceId.get(resourceId);     }

	public HAPIdEntityInDomain addEntityOrReference(HAPEntityOrReference entityOrRef, String entityType) {
		HAPInfoEntityInDomainDefinition entityInfo = HAPUtilityEntityDefinition.newEntityDefinitionInfoInDomain(entityType, this.m_entityDefMan); 
		return this.addEntityOrReference(entityOrRef, entityInfo);
	}
	
	public HAPIdEntityInDomain addEntity(HAPDefinitionEntityInDomain entity, HAPInfoEntityInDomainDefinition entityInfo) {
		HAPInfoEntityInDomainDefinition out = HAPUtilityEntityDefinition.newEntityDefinitionInfoInDomain(entity.getEntityType(), this.m_entityDefMan);
		entityInfo.cloneToInfoDefinitionEntityInDomain(out);
		out.setEntity(entity);
		out.setEntityId(this.newEntityId(entity.getEntityType()));
		this.m_entity.put(out.getEntityId(), out);
		return out.getEntityId();
	}

	public HAPIdEntityInDomain addEntity(HAPDefinitionEntityInDomain entity) {
		HAPInfoEntityInDomainDefinition entityInfo = HAPUtilityEntityDefinition.newEntityDefinitionInfoInDomain(entity.getEntityType(), this.m_entityDefMan); 
		return this.addEntity(entity, entityInfo);
	}

	public void setEntity(HAPIdEntityInDomain entityId, HAPDefinitionEntityInDomain entity) {
		HAPInfoEntityInDomainDefinition entityInfo = this.getEntityInfoDefinition(entityId);
		entityInfo.setEntity(entity);
	}
	
	private HAPIdEntityInDomain addEntityOrReference(HAPEntityOrReference entityOrRef, HAPInfoEntityInDomainDefinition entityInfo) {
		HAPInfoEntityInDomainDefinition out = HAPUtilityEntityDefinition.newEntityDefinitionInfoInDomain(entityInfo.getEntityType(), this.m_entityDefMan); 
		entityInfo.cloneToInfoDefinitionEntityInDomain(out);
		String entityType = null; 
		String type = entityOrRef.getEntityOrReferenceType();
		if(type.equals(HAPConstantShared.ENTITY)) {
			HAPDefinitionEntityInDomain entity = (HAPDefinitionEntityInDomain)entityOrRef;
			out.setEntity(entity);
			entityType = entity.getEntityType();
		}
		else if(type.equals(HAPConstantShared.RESOURCEID)) {
			HAPResourceId resourceId = (HAPResourceId)entityOrRef;
			out.setResourceId(resourceId);
			entityType = resourceId.getResourceType();
		}
		else if(type.equals(HAPConstantShared.REFERENCE)) {
			HAPReferenceAttachment attachmentRef = (HAPReferenceAttachment)entityOrRef;
			out.setAttachmentReference(attachmentRef);
			entityType = attachmentRef.getDataType();
		}
		out.setEntityId(newEntityId(entityType));
		this.m_entity.put(out.getEntityId(), out);
		return out.getEntityId();
	}
	
	private String generateId() {    return this.m_idGenerator.generateId();    }

	private HAPIdEntityInDomain newEntityId(String entityType) {
		return new HAPIdEntityInDomain(this.generateId(), entityType, this.m_domainId);
	}
	
	@Override
	protected void buildJsonMap(Map<String, String> jsonMap, Map<String, Class<?>> typeJsonMap){
		List<String> entityArray = new ArrayList<String>(); 
		for(HAPInfoEntityInDomainDefinition entity : this.m_entity.values()) {
			entityArray.add(entity.toStringValue(HAPSerializationFormat.JSON));
		}
		jsonMap.put(ENTITY, HAPJsonUtility.buildArrayJson(entityArray.toArray(new String[0])));
	}
}
