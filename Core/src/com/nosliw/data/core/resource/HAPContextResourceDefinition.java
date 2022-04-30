package com.nosliw.data.core.resource;

import com.nosliw.data.core.component.HAPPathLocationBase;
import com.nosliw.data.core.domain.HAPDomainEntityDefinitionResource;

public class HAPContextResourceDefinition {

	//entity domain
	private HAPDomainEntityDefinitionResource m_complexEntityDomain;
	
	//local path base for local reference
	private HAPPathLocationBase m_localRefBase;
	
	public HAPDomainEntityDefinitionResource getComplexEntityDomain() {    return this.m_complexEntityDomain;     }
	
	public HAPPathLocationBase getLocalReferenceBase() {     return this.m_localRefBase;       }
	
}
