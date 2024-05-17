package com.nosliw.core.application.common.interactive1;

import java.util.ArrayList;
import java.util.List;

import com.nosliw.common.utils.HAPConstantShared;
import com.nosliw.core.application.common.interactive.HAPResultInInteractiveInterface;
import com.nosliw.core.application.common.interactive.HAPResultOutputInInteractiveInterface;
import com.nosliw.core.application.common.structure.HAPElementStructureLeafData;
import com.nosliw.core.application.common.structure.reference.HAPContextStructureReference;
import com.nosliw.core.application.common.valueport.HAPInfoValueStructureReference;
import com.nosliw.core.application.common.valueport.HAPReferenceValueStructure;
import com.nosliw.core.application.division.manual.brick.valuestructure.HAPManualBrickValueStructure;
import com.nosliw.core.application.valuestructure.HAPRootStructure;

public class HAPContextStructureReferenceInteractiveResult implements HAPContextStructureReference{

	private HAPResultInInteractiveInterface m_result;
	
	private HAPManualBrickValueStructure m_valueStructureDef;
	
	public HAPContextStructureReferenceInteractiveResult(HAPResultInInteractiveInterface result) {
		this.m_result = result;
		this.buildValueStructure();
	}
	
	@Override
	public List<HAPInfoValueStructureReference> discoverCandidateValueStructure(HAPReferenceValueStructure valueStructureRef) {
		List<HAPInfoValueStructureReference> out = new ArrayList<HAPInfoValueStructureReference>();
		out.add(new HAPInfoValueStructureReference(HAPConstantShared.GLOBAL_VALUE_DEFAULT, this.m_valueStructureDef));
		return out;
	}

	@Override
	public HAPManualBrickValueStructure getValueStructureDefintion(String valueStructureId) {
		return this.m_valueStructureDef;
	}

	private void buildValueStructure() {
		this.m_valueStructureDef = new HAPManualBrickValueStructure();
		for(HAPResultOutputInInteractiveInterface output : this.m_result.getOutput()) {
			m_valueStructureDef.addRoot(new HAPRootStructure(new HAPElementStructureLeafData(output.getCriteria()), output));
		}
	}
}
