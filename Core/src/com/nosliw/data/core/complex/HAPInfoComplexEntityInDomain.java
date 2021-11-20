package com.nosliw.data.core.complex;

import java.util.Set;

public class HAPInfoComplexEntityInDomain {

	//entity
	private HAPDefinitionEntityComplex m_complexEntity;

	//alias for this entity
	private Set<String> m_alias;
	
	//parent info for this entity
	private HAPInfoParentEntity m_parentInfo;
	
	public HAPInfoComplexEntityInDomain(HAPDefinitionEntityComplex complexEntity, HAPInfoParentEntity parentInfo) {
		
	}

	public HAPDefinitionEntityComplex getComplexEntity() {   return this.m_complexEntity;   }
	
	public HAPInfoParentEntity getParentInfo() {    return this.m_parentInfo;    }
	
	public Set<String> getAlias(){   return this.m_alias;    }
}
