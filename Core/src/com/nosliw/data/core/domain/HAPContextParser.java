package com.nosliw.data.core.domain;

public class HAPContextParser {

	//global domain
	private HAPDomainEntityDefinitionGlobal m_globalDomain;
	
	private String m_currentDomainId;

	public HAPContextParser(HAPDomainEntityDefinitionGlobal globalDomain, String currentDomainId) {
		this.m_globalDomain = globalDomain;
		this.m_currentDomainId = currentDomainId;
	}
	
	public HAPDomainEntityDefinitionGlobal getGlobalDomain() {    return this.m_globalDomain;      }

	public HAPDomainEntityDefinitionSimpleResource getCurrentDomain() {    return this.m_globalDomain.getResourceDomainById(m_currentDomainId);     }
	public String getCurrentDomainId() {    return this.m_currentDomainId;    }
}
