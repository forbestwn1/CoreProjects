package com.nosliw.core.application.common.interactive1;

import java.util.ArrayList;
import java.util.List;

import com.nosliw.common.utils.HAPConstantShared;
import com.nosliw.core.application.common.interactive.HAPInteractiveResultTask;
import com.nosliw.core.application.common.interactive.HAPResultElementInInteractiveTask;
import com.nosliw.core.application.common.structure.HAPElementStructureLeafData;
import com.nosliw.core.xxx.application.common.structure.HAPRootStructure;
import com.nosliw.core.xxx.application.common.structure.reference.HAPContextStructureReference;
import com.nosliw.core.xxx.application.valueport.HAPInfoValueStructureReference;
import com.nosliw.core.xxx.application.valueport.HAPReferenceValueStructure;
import com.nosliw.core.xxx.application1.division.manual.brick.valuestructure.HAPManualBrickValueStructure;

public class HAPContextStructureReferenceInteractiveResult implements HAPContextStructureReference{

	private HAPInteractiveResultTask m_result;
	
	private HAPManualBrickValueStructure m_valueStructureDef;
	
	public HAPContextStructureReferenceInteractiveResult(HAPInteractiveResultTask result) {
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
		for(HAPResultElementInInteractiveTask output : this.m_result.getOutput()) {
			m_valueStructureDef.addRoot(new HAPRootStructure(new HAPElementStructureLeafData(output.getCriteria()), output));
		}
	}
}
