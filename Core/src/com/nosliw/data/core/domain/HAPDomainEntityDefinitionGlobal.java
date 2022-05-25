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
	private Map<String, HAPDomainEntityDefinitionSimpleResource> m_resourceDomainById;
	
	//domain id by resource id
	private Map<HAPResourceIdSimple, String> m_resourceDomainIdByResourceId;
	
	//resource definition by resource id
	private Map<HAPResourceId, HAPResourceDefinition> m_resourceDefByResourceId;

	public HAPDomainEntityDefinitionGlobal(HAPGeneratorId idGenerator, HAPManagerDomainEntityDefinition entityDefMan, HAPManagerResourceDefinition resourceDefinitionManager) {
		this.m_idGenerator = idGenerator;
		this.m_entityDefManager = entityDefMan;
		this.m_resourceDefinitionManager = resourceDefinitionManager;
		this.m_resourceDomainById = new LinkedHashMap<String, HAPDomainEntityDefinitionSimpleResource>();
		this.m_resourceDomainIdByResourceId = new LinkedHashMap<HAPResourceIdSimple, String>();
		this.m_resourceDefByResourceId = new LinkedHashMap<HAPResourceId, HAPResourceDefinition>();
	}

	public Set<HAPResourceIdSimple> getAllSimpleResourceIds(){    return this.m_resourceDomainIdByResourceId.keySet();     }
	public HAPInfoEntityInDomainDefinition getEntityInfoDefinition(HAPIdEntityInDomain entityId) {		return this.getResourceDomainById(entityId.getDomainId()).getEntityInfoDefinition(entityId);	}
	public HAPDomainEntityDefinitionSimpleResource getResourceDomainById(String id) {		return this.m_resourceDomainById.get(id);	}
	public String getDomainIdBySimpleResourceId(HAPResourceIdSimple resourceId) {   return this.m_resourceDomainIdByResourceId.get(resourceId);      }
	public HAPDomainEntityDefinitionSimpleResource getResourceDomainBySimpleResourceId(HAPResourceIdSimple resourceId) {		return this.getResourceDomainById(this.getDomainIdBySimpleResourceId(resourceId));	}
	
	public HAPResourceDefinition getResourceDefinitionByResourceId(HAPResourceId resourceId) {		return this.m_resourceDefByResourceId.get(resourceId);	}
	
	public HAPDomainEntityDefinitionSimpleResource newResourceDomain(HAPResourceIdSimple resourceId) {
		if(this.getResourceDomainByResourceId(resourceId)!=null)   throw new RuntimeException();
		HAPDomainEntityDefinitionSimpleResource out = null;
		if(m_entityDefManager.isComplexEntity(resourceId.getResourceType())) {
			out = new HAPDomainEntityDefinitionSimpleResourceComplex(resourceId, m_idGenerator, m_entityDefManager);
		}
		else {
			out = new HAPDomainEntityDefinitionSimpleResource(resourceId, m_idGenerator, m_entityDefManager);
		}
		this.m_resourceDomainById.put(out.getDomainId(), out);
		this.m_resourceDomainIdByResourceId.put(resourceId, out.getDomainId());
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
	
	private HAPDomainEntityDefinitionSimpleResource getResourceDomainByResourceId(HAPResourceIdSimple resourceId) {
		HAPDomainEntityDefinitionSimpleResource out = null;
		String domainId = this.m_resourceDomainIdByResourceId.get(resourceId);
		if(domainId!=null)  out = this.m_resourceDomainById.get(domainId);
		return out;
	}
	
	public HAPInfoParentComplex getComplexEntityParentInfo(HAPIdEntityInDomain entityId) {    return ((HAPDomainEntityDefinitionSimpleResourceComplex)this.getResourceDomainById(entityId.getDomainId())).getParentInfo(entityId);     }

	
	public void addResourceDefinition(HAPResourceDefinition resourceDef) {		this.m_resourceDefByResourceId.put(resourceDef.getResourceId(), resourceDef);	}

	@Override
	public HAPInfoEntityInDomain getEntityInfo(HAPIdEntityInDomain entityId) {		return this.getEntityInfoDefinition(entityId);	}
	
}
