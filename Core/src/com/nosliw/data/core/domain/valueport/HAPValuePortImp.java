package com.nosliw.data.core.domain.valueport;

import java.util.List;

import com.nosliw.data.core.domain.entity.valuestructure.HAPDefinitionEntityValueStructure;
import com.nosliw.data.core.structure.reference.HAPInfoValueStructureReference;
import com.nosliw.data.core.structure.reference.HAPReferenceValueStructure;

public abstract class HAPValuePortImp implements HAPValuePort{

	private HAPIdValuePort m_valuePortId;
	
	private HAPInfoValuePort m_valuePortInfo;
	
	public HAPValuePortImp(HAPIdValuePort valuePortId, HAPInfoValuePort valuePortInfo) {
		this.m_valuePortId = valuePortId;
		this.m_valuePortInfo = valuePortInfo;
	}
	
	@Override
	public HAPIdValuePort getValuePortId() {     return this.m_valuePortId;    }

	@Override
	public HAPInfoValuePort getValuePortInfo() {    return this.m_valuePortInfo;     }
	
	@Override
	public abstract List<HAPInfoValueStructureReference> discoverCandidateValueStructure(HAPReferenceValueStructure valueStructureRef);
	
	@Override
	public abstract HAPDefinitionEntityValueStructure getValueStructureDefintion(String valueStructureId);

}
