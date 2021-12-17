package com.nosliw.data.core.domain;

import org.apache.commons.lang3.tuple.Triple;

import com.nosliw.data.core.complex.HAPDefinitionEntityComplex;
import com.nosliw.data.core.complex.HAPExecutableEntityComplex;
import com.nosliw.data.core.complex.valuestructure.HAPComplexValueStructure;

//
public class HAPResultExecutableEntityInDomain {

	private HAPContextDomain m_domainContext;
	
	private HAPIdEntityInDomain m_executableEntityId;

	public HAPResultExecutableEntityInDomain(HAPIdEntityInDomain executableEntityId, HAPContextDomain domainContext) {
		this.m_domainContext = domainContext;
		this.m_executableEntityId = executableEntityId;
	}
	
	public HAPInfoEntity getComplexEntityInfoByExecutableId(){
		Triple<HAPDefinitionEntityComplex, HAPExecutableEntityComplex, HAPComplexValueStructure> triple = this.m_domainContext.getComplexEntityInfoByExecutableId(this.m_executableEntityId);
		return triple;
	}

	public HAPContextDomain getDomainContext() {   return this.m_domainContext;   }
	
}
