package com.nosliw.data.core.domain;

import java.util.Set;

import com.nosliw.data.core.complex.HAPConfigureParentRelationComplex;
import com.nosliw.data.core.domain.entity.HAPDefinitionEntityInDomainComplex;

public class HAPInfoComplexEntityDefinition {

	//entity
	private HAPDefinitionEntityInDomainComplex m_complexEntity;

	//alias for this entity
	private Set<String> m_alias;
	
	//parent info for this entity
	private HAPConfigureParentRelationComplex m_parentInfo;
	
	public HAPInfoComplexEntityDefinition(HAPDefinitionEntityInDomainComplex complexEntity, HAPConfigureParentRelationComplex parentInfo) {
		this.m_complexEntity = complexEntity;
		this.m_parentInfo = parentInfo;
	}

	public HAPDefinitionEntityInDomainComplex getComplexEntity() {   return this.m_complexEntity;   }
	
	public HAPConfigureParentRelationComplex getParentInfo() {    return this.m_parentInfo;    }
	
	public Set<String> getAlias(){   return this.m_alias;    }

}
