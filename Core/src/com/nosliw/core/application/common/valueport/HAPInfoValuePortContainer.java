package com.nosliw.core.application.common.valueport;

import com.nosliw.core.application.valuestructure.HAPDomainValueStructure;

public class HAPInfoValuePortContainer {

	private HAPContainerValuePorts m_valuePortContainer;
	
	private HAPDomainValueStructure m_valueStructureDomain;
	
	public HAPInfoValuePortContainer(HAPContainerValuePorts valuePortContainer, HAPDomainValueStructure valueStructureDomain) {
		this.m_valuePortContainer = valuePortContainer;
		this.m_valueStructureDomain = valueStructureDomain;
	}
	
	public HAPContainerValuePorts getValuePortContainer() {   return this.m_valuePortContainer;    }
	
	public HAPDomainValueStructure getValueStructureDomain() {    return this.m_valueStructureDomain;      }
	
}
