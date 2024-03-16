package com.nosliw.data.core.domain;

import org.apache.commons.lang3.tuple.Triple;

import com.nosliw.core.application.division.manual.HAPManualEntityComplex;
import com.nosliw.core.application.division.manual.brick.valuestructure.HAPDefinitionEntityValueContext;
import com.nosliw.data.core.domain.entity.HAPExecutableEntityComplex;

//
public class HAPResultExecutableEntityInDomain {

	private HAPContextDomain m_domainContext;
	
	private HAPIdEntityInDomain m_executableEntityId;

	public HAPResultExecutableEntityInDomain(HAPIdEntityInDomain executableEntityId, HAPContextDomain domainContext) {
		this.m_domainContext = domainContext;
		this.m_executableEntityId = executableEntityId;
	}
	
	public HAPInfoEntityComplex getComplexEntityInfoByExecutableId(){
		Triple<HAPManualEntityComplex, HAPExecutableEntityComplex, HAPDefinitionEntityValueContext> triple = this.m_domainContext.getComplexEntityInfoByExecutableId(this.m_executableEntityId);
		return triple;
	}

	public HAPContextDomain getDomainContext() {   return this.m_domainContext;   }
	
}
