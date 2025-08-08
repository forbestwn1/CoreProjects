package com.nosliw.core.xxx.application.valueport;

import com.nosliw.core.application.common.structure22.HAPElementStructure;

public class HAPInfoElementResolve {

	private HAPIdElement m_elementId;
	
	private HAPElementStructure m_elementStructure;
	
	public HAPInfoElementResolve(HAPIdElement elementId, HAPElementStructure elementStructure) {
		this.m_elementId = elementId;
		this.m_elementStructure = elementStructure;
	}
	
	public HAPIdElement getElementId() {     return this.m_elementId;      }
	
	public HAPElementStructure getElementStructure() {     return this.m_elementStructure;       }
	
}
