package com.nosliw.data.core.resource;

import com.nosliw.data.core.component.HAPPathLocationBase;
import com.nosliw.data.core.domain.HAPDomainEntityDefinitionSimpleResource;

public class HAPContextResourceDefinition {

	//entity domain
	private HAPDomainEntityDefinitionSimpleResource m_complexEntityDomain;
	
	//local path base for local reference
	private HAPPathLocationBase m_localRefBase;
	
	public HAPDomainEntityDefinitionSimpleResource getComplexEntityDomain() {    return this.m_complexEntityDomain;     }
	
	public HAPPathLocationBase getLocalReferenceBase() {     return this.m_localRefBase;       }
	
}
