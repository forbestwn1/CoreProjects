package com.nosliw.data.core.domain;

import java.util.Set;

import com.nosliw.core.application.division.manual.HAPManualBrickComplex;
import com.nosliw.data.core.domain.entity.HAPConfigureParentRelationComplex;

public class HAPInfoComplexEntityDefinition {

	//entity
	private HAPManualBrickComplex m_complexEntity;

	//alias for this entity
	private Set<String> m_alias;
	
	//parent info for this entity
	private HAPConfigureParentRelationComplex m_parentInfo;
	
	public HAPInfoComplexEntityDefinition(HAPManualBrickComplex complexEntity, HAPConfigureParentRelationComplex parentInfo) {
		this.m_complexEntity = complexEntity;
		this.m_parentInfo = parentInfo;
	}

	public HAPManualBrickComplex getComplexEntity() {   return this.m_complexEntity;   }
	
	public HAPConfigureParentRelationComplex getParentInfo() {    return this.m_parentInfo;    }
	
	public Set<String> getAlias(){   return this.m_alias;    }

}
