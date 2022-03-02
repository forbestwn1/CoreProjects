package com.nosliw.data.core.domain;

import com.nosliw.data.core.complex.HAPConfigureParentRelationComplex;

public class HAPInfoParentComplex {

	//parent complex entity
	private HAPIdEntityInDomain m_parentId;
	private String m_parentAlias;

	//parent info for complex
	private HAPConfigureParentRelationComplex m_parentRelation;

	public HAPInfoParentComplex() {
		this.m_parentRelation = new HAPConfigureParentRelationComplex();
	}
	
	public HAPIdEntityInDomain getParentId() {    return this.m_parentId;   }
	
	public void setParentId(HAPIdEntityInDomain parentId) {    this.m_parentId = parentId;     }
	
	public HAPConfigureParentRelationComplex getParentRelationConfigure() {    return this.m_parentRelation;    }
	
	public void setParentRelationConfigure(HAPConfigureParentRelationComplex parentRelation) {    this.m_parentRelation = parentRelation;    }
	
	public void hardMerge(HAPInfoParentComplex parentInfo) {
		
	}
	
	@Override
	public HAPInfoDefinitionEntityInDomain cloneEntityDefinitionInfo() {
		HAPConfigureEntityInDomainComplex out = new HAPConfigureEntityInDomainComplex();
		this.cloneToInfoDefinitionEntityInDomain(out);
		return out;
	}
	
	@Override
	public void cloneToInfoDefinitionEntityInDomain(HAPInfoDefinitionEntityInDomain out) {
		super.cloneToInfoDefinitionEntityInDomain(out);
		if(out instanceof HAPConfigureEntityInDomainComplex) {
			this.m_parentId = ((HAPConfigureEntityInDomainComplex)out).getParentId();
		}
	}

}
