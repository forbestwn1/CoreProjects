package com.nosliw.data.core.structure.reference;

import com.nosliw.data.core.entity.division.manual.valuestructure.HAPDefinitionEntityValueStructure;

public class HAPInfoValueStructureReference {

	private String m_id;
	
	private HAPDefinitionEntityValueStructure m_valueStructureDef;
	
	public HAPInfoValueStructureReference(String id, HAPDefinitionEntityValueStructure valueStructureDef) {
		this.m_id = id;
		this.m_valueStructureDef = valueStructureDef;
	}
	
	public String getValueStructureId() {    return this.m_id;      }
	
	public HAPDefinitionEntityValueStructure getValueStructureDefinition() {     return this.m_valueStructureDef;       }

}
