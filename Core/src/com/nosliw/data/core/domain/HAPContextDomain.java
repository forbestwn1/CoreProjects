package com.nosliw.data.core.domain;

import java.util.LinkedHashMap;
import java.util.Map;

import com.nosliw.common.utils.HAPGeneratorId;
import com.nosliw.data.core.domain.entity.HAPExecutableEntityComplex;

//context related with domain
public class HAPContextDomain {

	//definition domain
	private HAPDomainEntityDefinitionGlobal m_definitionDomain;

	//executable domain
	private HAPDomainEntityExecutableGlobal m_executableDomain;

	private Map<String, String> m_exeDomainIdByDefDomainId;
	
	//connection between executable entity to definition entity
	private Map<HAPIdEntityInDomain, HAPIdEntityInDomain> m_definitionIdByExecutableId;

	private HAPGeneratorId m_idGenerator;
	
	public HAPContextDomain(HAPManagerDomainEntityDefinition domainEntityDefMan) {
		this.m_idGenerator = new HAPGeneratorId();
		this.m_definitionDomain = new HAPDomainEntityDefinitionGlobal(this.m_idGenerator, domainEntityDefMan);
		this.m_executableDomain = new HAPDomainEntityExecutableGlobal(this.m_idGenerator);
		this.m_definitionIdByExecutableId = new LinkedHashMap<HAPIdEntityInDomain, HAPIdEntityInDomain>(); 
	}
	
	public HAPDomainEntityDefinitionGlobal getDefinitionDomain() {   return this.m_definitionDomain;    }
	public HAPDomainEntityExecutableGlobal getExecutableDomain() {    return this.m_executableDomain;    }

//	public HAPIdEntityInDomain getDefinitionEntityIdByExecutableId(HAPIdEntityInDomain executableId) {  return this.m_definitionIdByExecutableId.get(executableId); 	}
	
	public HAPIdEntityInDomain addExecutableEntity(HAPIdEntityInDomain definitionEntityId, HAPExecutableEntityComplex executableEntity, HAPExtraInfoEntityInDomainExecutable extraInfo) {
		HAPIdEntityInDomain out = this.m_executableDomain.addExecutableEntity(executableEntity, extraInfo);
		this.m_definitionIdByExecutableId.put(out, extraInfo.getEntityDefinitionId());
		return out;
	}
}
