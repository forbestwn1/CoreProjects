package com.nosliw.data.core.domain;

import java.util.LinkedHashMap;
import java.util.Map;

import com.nosliw.common.utils.HAPGeneratorId;
import com.nosliw.data.core.complex.HAPExecutableEntityComplex;

//context related with domain
public class HAPContextDomain {

	//definition domain
	private HAPDomainEntityDefinitionResource m_definitionDomain;

	//executable domain
	private HAPDomainEntityExecutable m_executableDomain;
	
	//attachment domain
	private HAPDomainAttachment m_attachmentDomain;
	
	//connection between executable entity to definition entity
	private Map<HAPIdEntityInDomain, HAPIdEntityInDomain> m_definitionIdByExecutableId;

	private HAPGeneratorId m_idGenerator;
	
	public HAPContextDomain(HAPManagerDomainEntityDefinition domainEntityDefMan) {
		this.m_idGenerator = new HAPGeneratorId();
		this.m_definitionDomain = new HAPDomainEntityDefinitionResource(this.m_idGenerator, domainEntityDefMan);
		this.m_executableDomain = new HAPDomainEntityExecutable(this.m_idGenerator);
		this.m_attachmentDomain = new HAPDomainAttachment(this.m_idGenerator);
		this.m_definitionIdByExecutableId = new LinkedHashMap<HAPIdEntityInDomain, HAPIdEntityInDomain>(); 
	}
	
	public HAPDomainValueStructure getValueStructureDomain() {   return null;    }
	public HAPDomainEntityDefinitionResource getDefinitionDomain() {   return this.m_definitionDomain;    }
	public HAPDomainEntityExecutable getExecutableDomain() {    return this.m_executableDomain;    }
	public HAPDomainAttachment getAttachmentDomain() {   return this.m_attachmentDomain;     }

	public HAPIdEntityInDomain getDefinitionEntityIdByExecutableId(HAPIdEntityInDomain executableId) {  return this.m_definitionIdByExecutableId.get(executableId); 	}
	
	public HAPIdEntityInDomain addExecutableEntity(HAPExecutableEntityComplex executableEntity, HAPExtraInfoEntityInDomainExecutable extraInfo) {
		HAPIdEntityInDomain out = this.m_executableDomain.addExecutableEntity(executableEntity, extraInfo);
		this.m_definitionIdByExecutableId.put(out, extraInfo.getEntityDefinitionId());
		return out;
	}
}
