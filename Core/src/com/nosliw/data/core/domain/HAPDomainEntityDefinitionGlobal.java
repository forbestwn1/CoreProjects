package com.nosliw.data.core.domain;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;

import com.nosliw.common.info.HAPUtilityEntityInfo;
import com.nosliw.common.serialization.HAPSerializableImp;
import com.nosliw.common.utils.HAPGeneratorId;
import com.nosliw.data.core.domain.entity.attachment.HAPAttachmentEntity;
import com.nosliw.data.core.domain.entity.attachment.HAPDefinitionEntityContainerAttachment;
import com.nosliw.data.core.resource.HAPManagerResourceDefinition;
import com.nosliw.data.core.resource.HAPResourceDefinition;
import com.nosliw.data.core.resource.HAPResourceId;
import com.nosliw.data.core.resource.HAPResourceIdSimple;

//domain represent complex entity resource
//it consist domain for different related resource
public class HAPDomainEntityDefinitionGlobal extends HAPSerializableImp implements HAPDomainEntity{

	private HAPGeneratorId m_idGenerator;

	private HAPManagerResourceDefinition m_resourceDefinitionManager;
	
	private HAPManagerDomainEntityDefinition m_entityDefManager;
	
	//entity domain by domain id
	private Map<String, HAPDomainEntityDefinitionLocal> m_localDomainById;
	
	//local domain id by resource id
	private Map<HAPResourceIdSimple, String> m_localDomainIdByResourceId;
	
	//resource definition by resource id
	private Map<HAPResourceId, HAPResourceDefinition> m_resourceDefByResourceId;

	public HAPDomainEntityDefinitionGlobal(HAPManagerDomainEntityDefinition entityDefMan, HAPManagerResourceDefinition resourceDefinitionManager) {
		this.m_idGenerator = new HAPGeneratorId();
		this.m_entityDefManager = entityDefMan;
		this.m_resourceDefinitionManager = resourceDefinitionManager;
		this.m_localDomainById = new LinkedHashMap<String, HAPDomainEntityDefinitionLocal>();
		this.m_localDomainIdByResourceId = new LinkedHashMap<HAPResourceIdSimple, String>();
		this.m_resourceDefByResourceId = new LinkedHashMap<HAPResourceId, HAPResourceDefinition>();
	}

	public Set<HAPResourceIdSimple> getAllSimpleResourceIds(){    return this.m_localDomainIdByResourceId.keySet();     }
	public HAPInfoEntityInDomainDefinition getEntityInfoDefinition(HAPIdEntityInDomain entityId) {		return this.getLocalDomainById(entityId.getDomainId()).getEntityInfoDefinition(entityId);	}
	public HAPDomainEntityDefinitionLocal getLocalDomainById(String id) {		return this.m_localDomainById.get(id);	}
	public String getDomainIdBySimpleResourceId(HAPResourceIdSimple resourceId) {   return this.m_localDomainIdByResourceId.get(resourceId);      }
	public HAPDomainEntityDefinitionLocal getLocalDomainBySimpleResourceId(HAPResourceIdSimple resourceId) {		return this.getLocalDomainById(this.getDomainIdBySimpleResourceId(resourceId));	}
	
	public HAPResourceDefinition getResourceDefinitionByResourceId(HAPResourceId resourceId) {		return this.m_resourceDefByResourceId.get(resourceId);	}
	
	public HAPDomainEntityDefinitionLocal newLocalDomain(HAPResourceIdSimple resourceId) {
		if(this.getLocalDomainByResourceId(resourceId)!=null)   throw new RuntimeException();
		HAPDomainEntityDefinitionLocal out = null;
		if(m_entityDefManager.isComplexEntity(resourceId.getResourceType())) {
			out = new HAPDomainEntityDefinitionLocalComplex(resourceId, m_idGenerator, m_entityDefManager);
		}
		else {
			out = new HAPDomainEntityDefinitionLocal(resourceId, m_idGenerator, m_entityDefManager);
		}
		this.m_localDomainById.put(out.getDomainId(), out);
		this.m_localDomainIdByResourceId.put(resourceId, out.getDomainId());
		return out;
	}
	
	public HAPInfoEntityInDomainDefinition getSolidEntityInfoDefinition(HAPIdEntityInDomain entityId, HAPDefinitionEntityContainerAttachment attachmentContainer) {
		HAPInfoEntityInDomainDefinition out = this.getEntityInfoDefinition(entityId);
		if(out.getEntity()==null){
			if(out.getResourceId()!=null) {
				if(out.isLocalResourceReference() || out.isGlobalSimpleResourceReference()) {
					HAPIdEntityInDomain resourceEntityId = this.m_resourceDefinitionManager.getResourceDefinition(out.getResourceId(), this, entityId.getDomainId()).getEntityId();
					HAPInfoEntityInDomainDefinition entityInfo = this.getEntityInfoDefinition(resourceEntityId);
					HAPUtilityEntityInfo.softMerge(out.getExtraInfo(), entityInfo.getExtraInfo());
					out.setEntity(entityInfo.getEntity());
				}
			}
			else if(out.getAttachmentReference()!=null) {
				HAPAttachmentEntity attachment = (HAPAttachmentEntity)attachmentContainer.getElement(out.getAttachmentReference());
				Object entityObj = attachment.getEntity();
				out.setEntity(this.getEntityInfoDefinition(HAPUtilityParserEntity.parseEntity(entityObj, attachment.getValueType(), new HAPContextParser(this, entityId.getDomainId()), this.m_entityDefManager, null)).getEntity());
			}
		}
		return out;
	}
	
	private HAPDomainEntityDefinitionLocal getLocalDomainByResourceId(HAPResourceIdSimple resourceId) {
		HAPDomainEntityDefinitionLocal out = null;
		String domainId = this.m_localDomainIdByResourceId.get(resourceId);
		if(domainId!=null)  out = this.m_localDomainById.get(domainId);
		return out;
	}
	
	public HAPInfoParentComplex getComplexEntityParentInfo(HAPIdEntityInDomain entityId) {    return ((HAPDomainEntityDefinitionLocalComplex)this.getLocalDomainById(entityId.getDomainId())).getParentInfo(entityId);     }

	
	public void addResourceDefinition(HAPResourceDefinition resourceDef) {		this.m_resourceDefByResourceId.put(resourceDef.getResourceId(), resourceDef);	}

	@Override
	public HAPInfoEntityInDomain getEntityInfo(HAPIdEntityInDomain entityId) {		return this.getEntityInfoDefinition(entityId);	}
	
}
