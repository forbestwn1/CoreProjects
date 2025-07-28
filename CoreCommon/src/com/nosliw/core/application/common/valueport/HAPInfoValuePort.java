package com.nosliw.core.application.common.valueport;

import com.nosliw.core.application.valuestructure.HAPDomainValueStructure;

public class HAPInfoValuePort {

	private HAPValuePort m_valuePort;
	
	private HAPDomainValueStructure m_valueStructureDomain;
	
	public HAPInfoValuePort(HAPValuePort valuePort, HAPDomainValueStructure valueStructureDomain) {
		this.m_valuePort = valuePort;
		this.m_valueStructureDomain = valueStructureDomain;
	}
	
	public HAPValuePort getValuePort() {   return this.m_valuePort;    }
	
	public HAPDomainValueStructure getValueStructureDomain() {    return this.m_valueStructureDomain;      }
	
}
