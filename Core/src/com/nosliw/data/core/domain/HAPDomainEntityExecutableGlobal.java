package com.nosliw.data.core.domain;

import java.util.LinkedHashMap;
import java.util.Map;

import com.nosliw.common.serialization.HAPUtilityJson;
import com.nosliw.common.serialization.HAPSerializableImp;
import com.nosliw.common.serialization.HAPSerializationFormat;
import com.nosliw.common.utils.HAPGeneratorId;
import com.nosliw.data.core.complex.HAPExecutableEntityComplex;
import com.nosliw.data.core.resource.HAPResourceIdSimple;

public class HAPDomainEntityExecutableGlobal extends HAPSerializableImp implements HAPDomainEntity{

	private static String MAINENTITYID = "mainEntityId";
	
	//attachment domain
	private Map<String, HAPDomainAttachment> m_attachmentDomainByEntityDomainId;

	private Map<String, HAPDomainEntityExecutableResourceComplex> m_resourceDomainById;

	private Map<HAPResourceIdSimple, String> m_resourceDomainIdByResourceId;

	//main entity
	private HAPIdEntityInDomain m_mainComplexEntityId;

	private HAPGeneratorId m_idGenerator;

	public HAPDomainEntityExecutableGlobal(HAPGeneratorId idGenerator) {
		this.m_idGenerator = idGenerator;
		this.m_resourceDomainById = new LinkedHashMap<String, HAPDomainEntityExecutableResourceComplex>();
		this.m_attachmentDomainByEntityDomainId = new LinkedHashMap<String, HAPDomainAttachment>();
	}
	
	public HAPIdEntityInDomain getMainEntityId() {   return this.m_mainComplexEntityId;   }
	public void setMainEntityId(HAPIdEntityInDomain mainEntityId) {    this.m_mainComplexEntityId = mainEntityId;      }
	
	public HAPIdEntityInDomain addExecutableEntity(HAPExecutableEntityComplex executableEntity, HAPExtraInfoEntityInDomainExecutable extraInfo, String domainId) {
		return this.getDomainById(domainId).addExecutableEntity(executableEntity, extraInfo);
	}

	@Override
	public HAPInfoEntityInDomain getEntityInfo(HAPIdEntityInDomain entityId) {   return this.getEntityInfoExecutable(entityId);  }
	public HAPInfoEntityInDomainExecutable getEntityInfoExecutable(HAPIdEntityInDomain entityId) {	return this.getDomainById(entityId.getDomainId()).getEntityInfoExecutable(entityId);	}

	public HAPDomainEntityExecutableResourceComplex newResourceDomain(HAPResourceIdSimple resourceId) {
		if(this.getResourceDomainByResourceId(resourceId)!=null)   throw new RuntimeException();
		HAPDomainEntityExecutableResourceComplex out = new HAPDomainEntityExecutableResourceComplex(this.m_idGenerator.generateId(), m_idGenerator);
		this.m_resourceDomainById.put(out.getDomainId(), out);
		this.m_resourceDomainIdByResourceId.put(resourceId, out.getDomainId());
		return out;
	}
	
	private HAPDomainEntityExecutableResourceComplex getDomainById(String domainId) {    return this.m_resourceDomainById.get(domainId);     }
	
	private HAPDomainEntityExecutableResourceComplex getResourceDomainByResourceId(HAPResourceIdSimple resourceId) {
		HAPDomainEntityExecutableResourceComplex out = null;
		String domainId = this.m_resourceDomainIdByResourceId.get(resourceId);
		if(domainId!=null)  out = this.m_resourceDomainById.get(domainId);
		return out;
	}

	@Override
	public String toString() {
		Map<String, String> jsonMap = new LinkedHashMap<String, String>();
		jsonMap.put(MAINENTITYID, this.m_mainComplexEntityId.toStringValue(HAPSerializationFormat.JSON));
		jsonMap.put(ROOTENTITYID, this.getEntityInfoExecutable(m_mainComplexEntityId).toExpandedJsonString(this));
		return HAPUtilityJson.buildMapJson(jsonMap);
	}
}
