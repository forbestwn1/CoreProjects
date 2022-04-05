package com.nosliw.data.core.domain;

import com.nosliw.data.core.component.HAPPathLocationBase;

public class HAPContextParser {

	//local ref base for 
	private HAPPathLocationBase m_localRefBase;

	//definition domain
	private HAPDomainEntityDefinition m_definitionDomain;

	public HAPContextParser(HAPDomainEntityDefinition definitionDomain, HAPPathLocationBase localRefBase) {
		this.m_definitionDomain = definitionDomain;
		this.m_localRefBase = localRefBase;
	}
	
	public HAPDomainEntityDefinition getDefinitionDomain() {    return this.m_definitionDomain;      }
	
	public HAPPathLocationBase getLocalReferenceBase() {    return this.m_localRefBase;    }
}
