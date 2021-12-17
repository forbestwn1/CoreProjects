package com.nosliw.data.core.resource;

import com.nosliw.data.core.component.HAPLocalReferenceBase;
import com.nosliw.data.core.domain.HAPDomainDefinitionEntity;

public class HAPContextResourceDefinition {

	//entity domain
	private HAPDomainDefinitionEntity m_complexEntityDomain;
	
	//local path base for local reference
	private HAPLocalReferenceBase m_localRefBase;
	
	public HAPDomainDefinitionEntity getComplexEntityDomain() {    return this.m_complexEntityDomain;     }
	
	public HAPLocalReferenceBase getLocalReferenceBase() {     return this.m_localRefBase;       }
	
}
