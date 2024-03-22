package com.nosliw.core.application.common.valueport;

public class HAPInfoValueStructureReference {

	private String m_id;
	
	private HAPValueStructure m_valueStructure;
	
	public HAPInfoValueStructureReference(String id, HAPValueStructure valueStructure) {
		this.m_id = id;
		this.m_valueStructure = valueStructure;
	}
	
	public String getValueStructureId() {    return this.m_id;      }
	
	public HAPValueStructure getValueStructureDefinition() {     return this.m_valueStructure;       }

}
