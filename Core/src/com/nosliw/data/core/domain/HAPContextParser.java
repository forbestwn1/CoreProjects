package com.nosliw.data.core.domain;

import com.nosliw.data.core.component.HAPPathLocationBase;

public class HAPContextParser {

	//local ref base for 
	private HAPPathLocationBase m_localRefBase;

	//definition domain
	private HAPDomainDefinitionEntity m_definitionDomain;

	public HAPContextParser(HAPDomainDefinitionEntity definitionDomain, HAPPathLocationBase localRefBase) {
		this.m_definitionDomain = definitionDomain;
		this.m_localRefBase = localRefBase;
	}
	
	public HAPDomainDefinitionEntity getDefinitionDomain() {    return this.m_definitionDomain;      }
	
	public HAPPathLocationBase getLocalReferenceBase() {    return this.m_localRefBase;    }
}
