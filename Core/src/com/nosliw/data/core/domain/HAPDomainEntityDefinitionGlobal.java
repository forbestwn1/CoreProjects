package com.nosliw.data.core.domain;

import java.util.LinkedHashMap;
import java.util.Map;

import com.nosliw.common.serialization.HAPSerializableImp;
import com.nosliw.common.utils.HAPGeneratorId;
import com.nosliw.data.core.resource.HAPResourceDefinition;
import com.nosliw.data.core.resource.HAPResourceId;
import com.nosliw.data.core.resource.HAPResourceIdSimple;

public class HAPDomainEntityDefinitionGlobal extends HAPSerializableImp implements HAPDomainEntity{

	private HAPGeneratorId m_idGenerator;

	private HAPManagerDomainEntityDefinition m_entityDefManager;
	
	private Map<String, HAPDomainEntityDefinitionResource> m_resourceDomainById;
	
	private Map<HAPResourceIdSimple, String> m_resourceDomainIdByResourceId;
	
	private Map<HAPResourceId, HAPResourceDefinition> m_resourceDefByResourceId;

	public HAPDomainEntityDefinitionGlobal(HAPGeneratorId idGenerator, HAPManagerDomainEntityDefinition entityDefMan) {
		this.m_idGenerator = idGenerator;
		this.m_entityDefManager = entityDefMan;
		this.m_resourceDomainById = new LinkedHashMap<String, HAPDomainEntityDefinitionResource>();
		this.m_resourceDomainIdByResourceId = new LinkedHashMap<HAPResourceIdSimple, String>();
		this.m_resourceDefByResourceId = new LinkedHashMap<HAPResourceId, HAPResourceDefinition>();
	}

	public HAPDomainEntityDefinitionResource newResourceDomain(HAPResourceIdSimple resourceId) {
		if(this.getResourceDomainByResourceId(resourceId)!=null)   throw new RuntimeException();
		HAPDomainEntityDefinitionResource out = new HAPDomainEntityDefinitionResource(this.m_idGenerator.generateId(), m_idGenerator, m_entityDefManager);
		this.m_resourceDomainById.put(out.getDomainId(), out);
		this.m_resourceDomainIdByResourceId.put(resourceId, out.getDomainId());
		return out;
	}
	
	public HAPInfoEntityInDomainDefinition getEntityInfoDefinition(HAPIdEntityInDomain entityId) {
		return this.getResourceDomainById(entityId.getDomainId()).getEntityInfoDefinition(entityId);
	}
	
	public HAPDomainEntityDefinitionResource getResourceDomainById(String id) {
		return this.m_resourceDomainById.get(id);
	}
	
	private HAPDomainEntityDefinitionResource getResourceDomainByResourceId(HAPResourceIdSimple resourceId) {
		HAPDomainEntityDefinitionResource out = null;
		String domainId = this.m_resourceDomainIdByResourceId.get(resourceId);
		if(domainId!=null)  out = this.m_resourceDomainById.get(domainId);
		return out;
	}
	
	public HAPResourceDefinition getResourceDefinitionByResourceId(HAPResourceId resourceId) {
		return this.m_resourceDefByResourceId.get(resourceId);
	}
	
	public void setResourceDefinition(HAPResourceDefinition resourceDef, HAPResourceId resourceId) {
		this.m_resourceDefByResourceId.put(resourceId, resourceDef);
	}

	@Override
	public HAPInfoEntityInDomain getEntityInfo(HAPIdEntityInDomain entityId) {
		return this.getEntityInfoDefinition(entityId);
	}
	
}
