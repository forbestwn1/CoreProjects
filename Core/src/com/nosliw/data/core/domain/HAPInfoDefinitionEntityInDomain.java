package com.nosliw.data.core.domain;

import java.util.LinkedHashMap;
import java.util.Map;

import com.nosliw.common.serialization.HAPJsonUtility;
import com.nosliw.common.serialization.HAPSerializableImp;
import com.nosliw.common.serialization.HAPSerializationFormat;
import com.nosliw.data.core.component.HAPPathLocationBase;
import com.nosliw.data.core.domain.entity.attachment.HAPReferenceAttachment;
import com.nosliw.data.core.resource.HAPResourceId;

public class HAPInfoDefinitionEntityInDomain extends HAPSerializableImp{
	
	public static final String ENTITYTYPE = "entityType";

	//entity id
	public static final String ENTITYID = "entityId";
	
	//entity definition
	public static final String ENTITY = "entity";
	
	//resource id
	public static final String RESOURCEID = "resourceId";

	//resource id
	public static final String RESOURCE = "resource";

	//reference to attachment
	public static final String REFERENCE = "reference";

	//extra info definition
	public static final String EXTRA = "extra";
	
	//parent info definition
	public static final String PARENT = "parent";
	
	private String m_entityType;
	
	//location path base info
	private HAPPathLocationBase m_baseLocationPath;

	//entity object
	private HAPDefinitionEntityInDomain m_entity;
	
	private HAPReferenceAttachment m_reference;
	
	private HAPResourceId m_resourceId;
	
	//entity id
	private HAPIdEntityInDomain m_entityId;

	//extra info provided by user (name, description, alias, global id, info, ...)
	private HAPInfoDefinitionEntityInDomainExtra m_extraInfo;

	//calculated value from entity type
	private boolean m_isComplex;
	
	public HAPInfoDefinitionEntityInDomain() {
		this.m_extraInfo = new HAPInfoDefinitionEntityInDomainExtra();
		this.m_isComplex = false;
	}

	public HAPInfoDefinitionEntityInDomain(String entityType) {
		this();
		this.m_entityType = entityType;
	}
	
	public String getEntityType() {    return this.m_entityType;    }
	
	public HAPDefinitionEntityInDomain getEntity() {     return this.m_entity;     }
	public void setEntity(HAPDefinitionEntityInDomain entityDef) {   this.m_entity = entityDef;    }
	
	public HAPReferenceAttachment getAttachmentReference() {   return this.m_reference;    }
	public void setAttachmentReference(HAPReferenceAttachment attachmentRef) {    this.m_reference = attachmentRef;    }
	
	public HAPResourceId getResourceId() {    return this.m_resourceId;    }
	public void setResourceId(HAPResourceId resourceId) {   this.m_resourceId = resourceId;    }
	
	public HAPPathLocationBase getBaseLocationPath() {     return this.m_baseLocationPath;     }
	public void setBaseLocationPath(HAPPathLocationBase baseLocationPath) {     this.m_baseLocationPath = baseLocationPath;    }

	public HAPIdEntityInDomain getEntityId() {    return this.m_entityId;     }
	public void setEntityId(HAPIdEntityInDomain entityId) {    this.m_entityId = entityId;    }

	public HAPInfoDefinitionEntityInDomainExtra getExtraInfo() {   return this.m_extraInfo;      }
	
	public boolean isComplexEntity() {   return this.m_isComplex;   }
	public void setIsComplexEntity(boolean isComplex) {    this.m_isComplex = isComplex;     }
	
	public void cloneToInfoDefinitionEntityInDomain(HAPInfoDefinitionEntityInDomain out) {
		out.m_isComplex = this.m_isComplex;
		if(this.m_baseLocationPath!=null)  out.m_baseLocationPath = this.m_baseLocationPath.cloneLocalReferenceBase();
		if(this.m_entity!=null)  out.m_entity = this.m_entity.cloneEntityDefinitionInDomain();
		if(this.m_entityId!=null)   out.m_entityId = this.m_entityId.cloneIdEntityInDomain();
	}
	
	public HAPInfoDefinitionEntityInDomain cloneEntityDefinitionInfo() {
		HAPInfoDefinitionEntityInDomain out = new HAPInfoDefinitionEntityInDomain();
		this.cloneToInfoDefinitionEntityInDomain(out);
		return out;
	}
	
	public String toExpandedJsonString(HAPDomainDefinitionEntity entityDefDomain) {
		Map<String, String> jsonMap = new LinkedHashMap<String, String>();
		Map<String, Class<?>> typeJsonMap = new LinkedHashMap<String, Class<?>>();
		this.buildJsonMap(jsonMap, typeJsonMap);
		if(this.m_entity!=null)   jsonMap.put(ENTITY, this.m_entity.toExpandedJsonString(entityDefDomain));
		
		if(this.m_resourceId!=null) {
			jsonMap.put(ENTITY, entityDefDomain.getEntityInfo(entityDefDomain.getResourceDefinition(m_resourceId).getEntityId()).getEntity().toExpandedJsonString(entityDefDomain));
		}
		
		HAPInfoParentComplex parentInfo = entityDefDomain.getParentInfo(this.m_entityId);
		if(parentInfo!=null)   jsonMap.put(PARENT, parentInfo.toStringValue(HAPSerializationFormat.JSON));
		return HAPJsonUtility.buildMapJson(jsonMap, typeJsonMap);
	}
	
	@Override
	protected void buildJsonMap(Map<String, String> jsonMap, Map<String, Class<?>> typeJsonMap){
		jsonMap.put(ENTITYTYPE, this.m_entityType);
		if(this.m_entityId!=null)   jsonMap.put(ENTITYID, this.m_entityId.toStringValue(HAPSerializationFormat.JSON));
		if(this.m_resourceId!=null)  jsonMap.put(RESOURCEID, this.m_resourceId.toStringValue(HAPSerializationFormat.JSON));
		if(this.m_reference!=null)   jsonMap.put(REFERENCE, this.m_reference.toStringValue(HAPSerializationFormat.JSON));
		if(this.m_extraInfo!=null)   jsonMap.put(EXTRA, this.m_extraInfo.toStringValue(HAPSerializationFormat.JSON));
		if(this.m_entity!=null)   jsonMap.put(ENTITY, this.m_entity.toStringValue(HAPSerializationFormat.JSON));
	}
}
