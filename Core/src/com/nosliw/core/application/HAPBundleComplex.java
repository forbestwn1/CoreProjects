package com.nosliw.core.application;

import com.nosliw.data.core.entity.valuestructure.HAPDomainValueStructure;

public class HAPBundleComplex extends HAPBundle{

	//processed value structure
	private HAPDomainValueStructure m_valueStructureDomain;

	public HAPBundleComplex() {
		this.m_valueStructureDomain = new HAPDomainValueStructure();
	}
	
	public HAPDomainValueStructure getValueStructureDomain() {
		return this.m_valueStructureDomain;
	}
	
}
