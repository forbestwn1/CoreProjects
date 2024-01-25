package com.nosliw.data.core.domain.valueport;

import java.util.List;

import com.nosliw.data.core.domain.entity.valuestructure.HAPDefinitionEntityValueStructure;
import com.nosliw.data.core.structure.reference.HAPInfoValueStructureReference;

public abstract class HAPValuePortImp implements HAPValuePort{

	private boolean m_isDefault = false;
	
	private HAPIdValuePort m_valuePortId;
	
	private HAPInfoValuePort m_valuePortInfo;
	
	public HAPValuePortImp(HAPIdValuePort valuePortId, HAPInfoValuePort valuePortInfo) {
		this(valuePortId, valuePortInfo, false);
	}

	public HAPValuePortImp(HAPIdValuePort valuePortId, HAPInfoValuePort valuePortInfo, boolean isDefault) {
		this.m_valuePortId = valuePortId;
		this.m_valuePortInfo = valuePortInfo;
		this.m_isDefault = isDefault;
	}

	@Override
	public boolean isDefault() {    return this.m_isDefault;    }
	
	@Override
	public HAPIdValuePort getValuePortId() {     return this.m_valuePortId;    }

	@Override
	public HAPInfoValuePort getValuePortInfo() {    return this.m_valuePortInfo;     }
	
	@Override
	public abstract List<HAPInfoValueStructureReference> discoverCandidateValueStructure(HAPReferenceValueStructure valueStructureRef);
	
	@Override
	public abstract HAPDefinitionEntityValueStructure getValueStructureDefintion(String valueStructureId);

}
