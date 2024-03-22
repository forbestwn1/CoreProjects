package com.nosliw.core.application.common.valueport;

import java.util.List;

import com.nosliw.common.info.HAPEntityInfoImp;

public abstract class HAPValuePortImp extends HAPEntityInfoImp implements HAPValuePort{

	private boolean m_isDefault = false;
	
	private HAPInfoValuePort m_valuePortInfo;
	
	public HAPValuePortImp(HAPIdValuePort valuePortId, HAPInfoValuePort valuePortInfo) {
		this(valuePortInfo, false);
	}

	public HAPValuePortImp(HAPInfoValuePort valuePortInfo, boolean isDefault) {
		this.m_valuePortInfo = valuePortInfo;
		this.m_isDefault = isDefault;
	}

	@Override
	public boolean isDefault() {    return this.m_isDefault;    }
	
	@Override
	public HAPInfoValuePort getValuePortInfo() {    return this.m_valuePortInfo;     }

	
	@Override
	public HAPResultReferenceResolve resolveReference(HAPReferenceElement elementReference, HAPConfigureResolveElementReference configure) {
		List<HAPInfoValueStructureReference> candiates = this.discoverCandidateValueStructure(elementReference.getValueStructureReference(), configure);
		return HAPUtilityStructureElementReference.analyzeElementReference(elementReference.getElementPath(), candiates, configure);
	}
	
	protected abstract List<HAPInfoValueStructureReference> discoverCandidateValueStructure(HAPReferenceValueStructure valueStructureCriteria, HAPConfigureResolveElementReference configure);
	
}
