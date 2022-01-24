package com.nosliw.data.core.domain;

import com.nosliw.data.core.component.HAPLocalReferenceBase;

public class HAPContextParser {

	//local ref base for 
	private HAPLocalReferenceBase m_localRefBase;

	//definition domain
	private HAPDomainDefinitionEntity m_definitionDomain;

	public HAPContextParser(HAPDomainDefinitionEntity definitionDomain, HAPLocalReferenceBase localRefBase) {
		this.m_definitionDomain = definitionDomain;
		this.m_localRefBase = localRefBase;
	}
	
	public HAPDomainDefinitionEntity getDefinitionDomain() {    return this.m_definitionDomain;      }
	
	public HAPLocalReferenceBase getLocalReferenceBase() {    return this.m_localRefBase;    }
}
