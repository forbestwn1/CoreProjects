package com.nosliw.data.core.domain;

import java.util.Map;

import com.nosliw.data.core.complex.HAPExecutableEntityComplex;
import com.nosliw.data.core.resource.HAPInfoResourceIdNormalize;
import com.nosliw.data.core.resource.HAPResourceIdSimple;

//all information for complex resource 
public class HAPPackageComplexResource {

	//entity domain definition
	private HAPDomainEntityDefinitionGlobal m_definitionDomain;
	
	//entity domain executable
	private HAPDomainEntityExecutableResourceComplex m_executableDomain;

	//mapping between complex entity definition id to executable id 
	private Map<HAPIdEntityInDomain, HAPIdEntityInDomain> m_executableComplexEntityIdByDefinitionComplexEntityId;
	
	public HAPPackageComplexResource(HAPResourceIdSimple rootResourceId, HAPDomainEntityDefinitionGlobal m_definitionDomain) {
		
	}
	
	public HAPResourceIdSimple getRootResourceId() {    return this.m_definitionDomain.getRootResourceId();     }
	
	public HAPDomainEntityDefinitionGlobal getDefinitionDomain() {		return this.m_definitionDomain; 	}
	
	public HAPDomainEntityExecutableResourceComplex getExecutableDomain() {    return this.m_executableDomain;     }
	
	public HAPIdEntityInDomain addExecutableEntity(HAPIdEntityInDomain definitionEntityId, HAPExecutableEntityComplex executableEntity, HAPExtraInfoEntityInDomainExecutable extraInfo) {
		HAPIdEntityInDomain out = this.m_executableDomain.addExecutableEntity(executableEntity, extraInfo);
		this.m_executableComplexEntityIdByDefinitionComplexEntityId.put(out, extraInfo.getEntityDefinitionId());
		return out;
	}

	public HAPIdEntityInDomain addExecutableEntity(HAPIdEntityInDomain definitionEntityId, HAPInfoResourceIdNormalize normalizedResourceId, HAPExtraInfoEntityInDomainExecutable extraInfo) {
		HAPIdEntityInDomain out = this.m_executableDomain.addExecutableEntity(normalizedResourceId, extraInfo);
		this.m_executableComplexEntityIdByDefinitionComplexEntityId.put(out, extraInfo.getEntityDefinitionId());
		return out;
	}

}
