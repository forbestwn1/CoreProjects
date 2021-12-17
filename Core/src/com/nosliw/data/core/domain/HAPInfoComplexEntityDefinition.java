package com.nosliw.data.core.domain;

import java.util.Set;

import com.nosliw.data.core.complex.HAPDefinitionEntityComplex;
import com.nosliw.data.core.complex.HAPConfigureParentRelationComplex;

public class HAPInfoComplexEntityDefinition {

	//entity
	private HAPDefinitionEntityComplex m_complexEntity;

	//alias for this entity
	private Set<String> m_alias;
	
	//parent info for this entity
	private HAPConfigureParentRelationComplex m_parentInfo;
	
	public HAPInfoComplexEntityDefinition(HAPDefinitionEntityComplex complexEntity, HAPConfigureParentRelationComplex parentInfo) {
		this.m_complexEntity = complexEntity;
		this.m_parentInfo = parentInfo;
	}

	public HAPDefinitionEntityComplex getComplexEntity() {   return this.m_complexEntity;   }
	
	public HAPConfigureParentRelationComplex getParentInfo() {    return this.m_parentInfo;    }
	
	public Set<String> getAlias(){   return this.m_alias;    }

}
