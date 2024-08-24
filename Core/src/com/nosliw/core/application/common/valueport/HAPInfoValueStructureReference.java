package com.nosliw.core.application.common.valueport;

import com.nosliw.core.application.valuestructure.HAPDefinitionStructure;

public class HAPInfoValueStructureReference {

	private String m_id;
	
	private HAPDefinitionStructure m_structure;
	
	public HAPInfoValueStructureReference(String id, HAPDefinitionStructure structure) {
		this.m_id = id;
		this.m_structure = structure;
	}
	
	public String getValueStructureId() {    return this.m_id;      }
	
	public HAPDefinitionStructure getStructureDefinition() {     return this.m_structure;       }

}
