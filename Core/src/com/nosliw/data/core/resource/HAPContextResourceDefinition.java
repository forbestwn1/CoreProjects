package com.nosliw.data.core.resource;

import com.nosliw.data.core.complex.HAPDomainDefinitionComplex;
import com.nosliw.data.core.component.HAPLocalReferenceBase;

public class HAPContextResourceDefinition {

	//entity domain
	private HAPDomainDefinitionComplex m_complexEntityDomain;
	
	//local path base for local reference
	private HAPLocalReferenceBase m_localRefBase;
	
	public HAPDomainDefinitionComplex getComplexEntityDomain() {    return this.m_complexEntityDomain;     }
	
	public HAPLocalReferenceBase getLocalReferenceBase() {     return this.m_localRefBase;       }
	
}
