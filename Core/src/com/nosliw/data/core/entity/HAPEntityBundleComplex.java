package com.nosliw.data.core.entity;

import com.nosliw.data.core.entity.valuestructure.HAPDomainValueStructure;

public class HAPEntityBundleComplex extends HAPEntityBundle{

	//processed value structure
	private HAPDomainValueStructure m_valueStructureDomain;

	public HAPEntityBundleComplex() {
		this.m_valueStructureDomain = new HAPDomainValueStructure();
	}
	
	public HAPDomainValueStructure getValueStructureDomain() {
		return this.m_valueStructureDomain;
	}
	
}
