package com.nosliw.core.application.common.valueport;

import com.nosliw.core.application.common.structure.HAPStructureImp;

public class HAPInfoValueStructureReference {

	private String m_id;
	
	private HAPStructureImp m_structure;
	
	public HAPInfoValueStructureReference(String id, HAPStructureImp structure) {
		this.m_id = id;
		this.m_structure = structure;
	}
	
	public String getValueStructureId() {    return this.m_id;      }
	
	public HAPStructureImp getStructureDefinition() {     return this.m_structure;       }

}
