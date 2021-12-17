package com.nosliw.data.core.domain;

import com.nosliw.data.core.complex.HAPConfigureParentRelationComplex;
import com.nosliw.data.core.complex.HAPDefinitionEntityComplex;

public class HAPInfoDefinitionEntityInDomainComplex extends HAPInfoDefinitionEntityInDomain{

	//parent complex entity
	private HAPIdEntityInDomain m_parentId;
	private String m_parentAlias;


	//parent info for complex
	private HAPConfigureParentRelationComplex m_parentRelation;

	public HAPIdEntityInDomain getParentId() {    return this.m_parentId;   }
	
	public void setParentId(HAPIdEntityInDomain parentId) {    this.m_parentId = parentId;     }
	
	public HAPConfigureParentRelationComplex getParentRelationConfigure() {    return this.m_parentRelation;    }
	
	public void setParentRelationConfigure(HAPConfigureParentRelationComplex parentRelation) {    this.m_parentRelation = parentRelation;    }
	
	public HAPDefinitionEntityComplex getComplexEntity() {     return (HAPDefinitionEntityComplex)this.getEntity();     }

}
