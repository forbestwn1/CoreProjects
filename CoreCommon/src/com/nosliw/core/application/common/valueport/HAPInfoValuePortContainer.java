package com.nosliw.core.application.common.valueport;

import org.apache.commons.lang3.tuple.Pair;

import com.nosliw.core.application.valuestructure.HAPDomainValueStructure;

public class HAPInfoValuePortContainer {

	private Pair<HAPContainerValuePorts, HAPContainerValuePorts> m_valuePortContainer;
	
	private HAPDomainValueStructure m_valueStructureDomain;

	private boolean m_internal;
	
	public HAPInfoValuePortContainer(Pair<HAPContainerValuePorts, HAPContainerValuePorts> valuePortContainerPair, HAPDomainValueStructure valueStructureDomain, boolean internal) {
		this.m_valuePortContainer = valuePortContainerPair;
		this.m_valueStructureDomain = valueStructureDomain;
		this.m_internal = internal;
	}
	
	public Pair<HAPContainerValuePorts, HAPContainerValuePorts> getValuePortContainerPair() {   return this.m_valuePortContainer;    }
	
	public HAPDomainValueStructure getValueStructureDomain() {    return this.m_valueStructureDomain;      }
	
	public boolean isInternal() {    return this.m_internal;     }
	
}
