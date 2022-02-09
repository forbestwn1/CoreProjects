package com.nosliw.data.core.domain;

import java.util.Map;

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
	private Map<String, String> m_definitionIdByExecutableId;

	public HAPContextDomain() {
		
	}
	
	public HAPDomainValueStructure getValueStructureDomain() {   return null;    }
	public HAPDomainDefinitionEntity getDefinitionDomain() {   return this.m_definitionDomain;    }
	public HAPDomainExecutableEntity getExecutableDomain() {    return this.m_executableDomain;    }
	
	public HAPIdEntityInDomain addExecutableEntity(HAPExecutableEntityComplex executableEntity, HAPIdEntityInDomain definitionId) {
		
	}

	public HAPExecutableEntityComplex getExecutableEntityByExecutableId(HAPIdEntityInDomain executableId) {
		
	}
	
	public HAPDefinitionEntityComplex getDefinitionEntityByExecutableId(HAPIdEntityInDomain executableId) {
		
	}

	public HAPIdEntityInDomain getExecutableIdByDefinitionId(HAPIdEntityInDomain definitionEntityId) {
		
	}
	

	public HAPDefinitionEntityComplex getDefinitionComplexEntity(HAPIdEntityInDomain entityId) {
		
	}
	
	public HAPInfoEntityComplex getComplexEntityInfoByExecutableId(HAPIdEntityInDomain executableId){
		
	}
}