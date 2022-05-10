package com.nosliw.data.core.domain;

import java.util.LinkedHashMap;
import java.util.Map;

import com.nosliw.common.serialization.HAPJsonUtility;
import com.nosliw.common.serialization.HAPSerializableImp;
import com.nosliw.common.serialization.HAPSerializationFormat;
import com.nosliw.common.utils.HAPConstantShared;
import com.nosliw.data.core.domain.entity.attachment.HAPReferenceAttachment;
import com.nosliw.data.core.resource.HAPResourceId;

public class HAPInfoEntityInDomainDefinition extends HAPSerializableImp implements HAPInfoEntityInDomain{
	
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
	
	//entity object
	private HAPDefinitionEntityInDomain m_entity;
	
	private HAPReferenceAttachment m_reference;
	
	private HAPResourceId m_resourceId;
	
	//entity id
	private HAPIdEntityInDomain m_entityId;

	//extra info provided by user (name, description, alias, global id, info, ...)
	private HAPExtraInfoEntityInDomainDefinition m_extraInfo;

	//calculated value from entity type
	private boolean m_isComplex;
	
	public HAPInfoEntityInDomainDefinition() {
		this.m_extraInfo = new HAPExtraInfoEntityInDomainDefinition();
		this.m_isComplex = false;
	}

	public HAPInfoEntityInDomainDefinition(String entityType) {
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
	
	public HAPIdEntityInDomain getEntityId() {    return this.m_entityId;     }
	public void setEntityId(HAPIdEntityInDomain entityId) {    this.m_entityId = entityId;    }

	public HAPExtraInfoEntityInDomainDefinition getExtraInfo() {   return this.m_extraInfo;      }
	
	public boolean isComplexEntity() {   return this.m_isComplex;   }
	public void setIsComplexEntity(boolean isComplex) {    this.m_isComplex = isComplex;     }
	
	public boolean isSolid() {   return this.m_entity!=null;   }
	public boolean isAttachmentReference() {   return this.m_reference!=null;    }
	public boolean isLocalResourceReference() {    
		if(this.m_resourceId==null)  return false;
		if(this.m_resourceId.getStructure().equals(HAPConstantShared.RESOURCEID_TYPE_LOCAL)) return true;
		return false;
	}
	public boolean isGlobalResourceReference() {	return !this.isLocalResourceReference();	}
	public boolean isGlobalSimpleResourceReference() {     return !this.isLocalResourceReference() && !this.isComplexEntity();        }
	public boolean isGlobalComplexResourceReference() {     return !this.isLocalResourceReference() && this.isComplexEntity();        }
	
	public void cloneToInfoDefinitionEntityInDomain(HAPInfoEntityInDomainDefinition out) {
		out.m_isComplex = this.m_isComplex;
		out.m_entityType = this.m_entityType;
		if(this.m_entity!=null)  out.m_entity = this.m_entity.cloneEntityDefinitionInDomain();
		if(this.m_entityId!=null)   out.m_entityId = this.m_entityId.cloneIdEntityInDomain();
		if(this.m_resourceId!=null)  out.m_resourceId = this.m_resourceId.clone();
		if(this.m_reference!=null)   out.m_reference = this.m_reference.cloneAttachmentReference();
		if(this.m_extraInfo!=null)  out.m_extraInfo = this.m_extraInfo.cloneExtraInfo();
	}
	
	public HAPInfoEntityInDomainDefinition cloneEntityDefinitionInfo() {
		HAPInfoEntityInDomainDefinition out = new HAPInfoEntityInDomainDefinition();
		this.cloneToInfoDefinitionEntityInDomain(out);
		return out;
	}
	
	@Override
	public String toExpandedJsonString(HAPDomainEntity entityDomain) {
		HAPDomainEntityDefinitionGlobal entityDefDomain = (HAPDomainEntityDefinitionGlobal)entityDomain;
		Map<String, String> jsonMap = new LinkedHashMap<String, String>();
		Map<String, Class<?>> typeJsonMap = new LinkedHashMap<String, Class<?>>();
		this.buildJsonMap(jsonMap, typeJsonMap);
		if(this.m_entity!=null)   jsonMap.put(ENTITY, this.m_entity.toExpandedJsonString(entityDefDomain));
		
		if(this.m_resourceId!=null) {
			HAPInfoEntityInDomainDefinition entityInfo = HAPUtilityDomain.getEntityInfoByResourceId(m_resourceId, this.getEntityId().getDomainId(), entityDefDomain);
			jsonMap.put(ENTITY, entityInfo.toExpandedJsonString(entityDefDomain));
		}
		
		HAPInfoParentComplex parentInfo = entityDefDomain.getResourceDomainById(this.m_entityId.getDomainId()).getParentInfo(this.m_entityId);
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
