package com.nosliw.data.core.domain;

import java.util.LinkedHashMap;
import java.util.Map;

import com.nosliw.common.utils.HAPGeneratorId;
import com.nosliw.data.core.complex.HAPDefinitionEntityComplex;
import com.nosliw.data.core.complex.HAPExecutableEntityComplex;

//context related with domain
public class HAPContextDomain {

	//definition domain
	private HAPDomainDefinitionEntity m_definitionDomain;

	//executable domain
	private HAPDomainExecutableEntity m_executableDomain;
	
	//value structure domain (value structure definition, runtime id)
	private HAPDomainValueStructure m_valueStructureDomain;
	
	//connection between executable entity to definition entity
	private Map<HAPIdEntityInDomain, HAPIdEntityInDomain> m_definitionIdByExecutableId;

	private HAPGeneratorId m_idGenerator;
	
	public HAPContextDomain(HAPManagerDomainEntityDefinition domainEntityDefMan) {
		this.m_idGenerator = new HAPGeneratorId();
		this.m_definitionDomain = new HAPDomainDefinitionEntity(this.m_idGenerator, domainEntityDefMan);
		this.m_executableDomain = new HAPDomainExecutableEntity(this.m_idGenerator);
		this.m_definitionIdByExecutableId = new LinkedHashMap<HAPIdEntityInDomain, HAPIdEntityInDomain>(); 
	}
	
	public HAPDomainValueStructure getValueStructureDomain() {   return null;    }
	public HAPDomainDefinitionEntity getDefinitionDomain() {   return this.m_definitionDomain;    }
	public HAPDomainExecutableEntity getExecutableDomain() {    return this.m_executableDomain;    }

	public HAPIdEntityInDomain addExecutableEntity(HAPExecutableEntityComplex executableEntity, HAPIdEntityInDomain definitionId) {
		HAPIdEntityInDomain out = this.m_executableDomain.addExecutableEntity(executableEntity);
		this.m_definitionIdByExecutableId.put(out, definitionId);
		return out;
	}

	public HAPIdEntityInDomain getExecutableIdByDefinitionId(HAPIdEntityInDomain definitionEntityId) {
		
		
	}
	
	public HAPExecutableEntityComplex getExecutableEntityByExecutableId(HAPIdEntityInDomain executableId) {
		
	}
	
	public HAPDefinitionEntityComplex getDefinitionEntityByExecutableId(HAPIdEntityInDomain executableId) {
		
	}


	public HAPDefinitionEntityComplex getDefinitionComplexEntity(HAPIdEntityInDomain entityId) {
		
	}
	
	public HAPInfoEntityComplex getComplexEntityInfoByExecutableId(HAPIdEntityInDomain executableId){
		
	}
}
