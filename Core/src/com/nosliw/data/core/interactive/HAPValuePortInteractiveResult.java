package com.nosliw.data.core.interactive;

import java.util.ArrayList;
import java.util.List;

import com.nosliw.common.utils.HAPConstantShared;
import com.nosliw.core.application.division.manual.brick.valuestructure.HAPDefinitionEntityValueStructure;
import com.nosliw.data.core.domain.entity.valuestructure.HAPRootStructure;
import com.nosliw.data.core.domain.valueport.HAPIdValuePort;
import com.nosliw.data.core.domain.valueport.HAPInfoValuePort;
import com.nosliw.data.core.domain.valueport.HAPReferenceValueStructure;
import com.nosliw.data.core.domain.valueport.HAPValuePortImp;
import com.nosliw.data.core.structure.HAPElementStructureLeafData;
import com.nosliw.data.core.structure.reference.HAPInfoValueStructureReference;

public class HAPValuePortInteractiveResult extends HAPValuePortImp{

	private HAPDefinitionInteractiveResult m_result;
	
	private HAPDefinitionEntityValueStructure m_valueStructureDef;
	
	public HAPValuePortInteractiveResult(HAPIdValuePort valuePortId, HAPInfoValuePort valuePortInfo, HAPDefinitionInteractiveResult result) {
		super(valuePortId, valuePortInfo);
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
	public HAPDefinitionEntityValueStructure getValueStructureDefintion(String valueStructureId) {
		return this.m_valueStructureDef;
	}

	private void buildValueStructure() {
		this.m_valueStructureDef = new HAPDefinitionEntityValueStructure();
		for(HAPDefinitionInteractiveResultOutput output : this.m_result.getOutput()) {
			m_valueStructureDef.addRoot(new HAPRootStructure(new HAPElementStructureLeafData(output.getCriteria()), output));
		}
	}
}
