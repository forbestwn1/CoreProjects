package com.nosliw.core.application.common.valueport;

import java.util.ArrayList;
import java.util.List;

public abstract class HAPValuePortImp implements HAPValuePort{

	private HAPInfoValuePort m_valuePortInfo;
	
	public HAPValuePortImp(HAPInfoValuePort valuePortInfo) {
		this.m_valuePortInfo = valuePortInfo;
	}

	@Override
	public HAPInfoValuePort getValuePortInfo() {    return this.m_valuePortInfo;     }

	
	@Override
	public HAPResultReferenceResolve resolveReference(HAPReferenceElement elementReference, HAPConfigureResolveElementReference configure) {
		List<HAPInfoValueStructureReference> candiateValueStructures = new ArrayList<HAPInfoValueStructureReference>(); 
		List<String> candiateIds = this.discoverCandidateValueStructure(elementReference.getValueStructureReference(), configure);
		for(String valueStructureId : candiateIds) {
			candiateValueStructures.add(new HAPInfoValueStructureReference(valueStructureId, this.getValueStructureDefintion(valueStructureId)));
		}
		HAPResultReferenceResolve out = HAPUtilityStructureElementReference.analyzeElementReference(elementReference.getElementPath(), candiateValueStructures, configure);
		return out;
	}
	
	protected abstract List<String> discoverCandidateValueStructure(HAPReferenceValueStructure valueStructureCriteria, HAPConfigureResolveElementReference configure);
	
}
