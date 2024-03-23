package com.nosliw.core.application.common.valueport;

import java.util.ArrayList;
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
		List<HAPInfoValueStructureReference> candiateValueStructures = new ArrayList<HAPInfoValueStructureReference>(); 
		List<String> candiateIds = this.discoverCandidateValueStructure(elementReference.getValueStructureReference(), configure);
		for(String valueStructureId : candiateIds) {
			candiateValueStructures.add(new HAPInfoValueStructureReference(valueStructureId, this.getValueStructureDefintion(valueStructureId)));
		}
		HAPResultReferenceResolve out = HAPUtilityStructureElementReference.analyzeElementReference(elementReference.getElementPath(), candiateValueStructures, configure);
		if(out!=null) {
			out.eleReference = elementReference;
		}
		return out;
	}
	
	protected abstract List<String> discoverCandidateValueStructure(HAPReferenceValueStructure valueStructureCriteria, HAPConfigureResolveElementReference configure);
	
}
