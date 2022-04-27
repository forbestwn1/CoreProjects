package com.nosliw.data.core.domain;

import com.nosliw.data.core.domain.entity.valuestructure.HAPDefinitionEntityValueStructure;

public class HAPInfoValueStructure {

	private HAPDefinitionEntityValueStructure m_valueStructure;
	
	private HAPExtraInfoEntityInDomainDefinition m_extraInfo;
	
	public HAPInfoValueStructure(HAPDefinitionEntityValueStructure valueStructure, HAPExtraInfoEntityInDomainDefinition extraInfo) {
		this.m_valueStructure = valueStructure;
		this.m_extraInfo = extraInfo;
	}
	
	public HAPDefinitionEntityValueStructure getValueStructure() {    return this.m_valueStructure;     }
	
	public HAPExtraInfoEntityInDomainDefinition getExtraInfo() {     return this.m_extraInfo;      }
	
}
