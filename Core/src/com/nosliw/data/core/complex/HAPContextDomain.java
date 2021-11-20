package com.nosliw.data.core.complex;

import java.util.Map;

//context related with domain
public class HAPContextDomain {

	//definition domain
	private HAPDomainDefinitionComplex m_definitionDomain;

	//executable domain
	private HAPDomainExecutableComplex m_executableDomain;
	
	//connection between executable entity to definition entity
	private Map<String, String> m_definitionIdByExecutableId;

	public String addExecutableEntity(HAPExecutableEntityComplex executableEntity, String definitionId) {
		
	}

	public HAPDefinitionEntityComplex getDefinitionEntityByExecutableId(String executableId) {
		
	}
	
}
