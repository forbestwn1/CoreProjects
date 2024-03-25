package com.nosliw.data.core.interactive;

import java.util.ArrayList;
import java.util.List;

import com.nosliw.common.utils.HAPConstantShared;
import com.nosliw.core.application.common.structure.HAPElementStructureLeafData;
import com.nosliw.core.application.common.valueport.HAPIdValuePort;
import com.nosliw.core.application.common.valueport.HAPInfoValuePort;
import com.nosliw.core.application.common.valueport.HAPInfoValueStructureReference;
import com.nosliw.core.application.common.valueport.HAPReferenceValueStructure;
import com.nosliw.core.application.common.valueport.HAPValuePortImp;
import com.nosliw.core.application.division.manual.brick.valuestructure.HAPManualBrickValueStructure;
import com.nosliw.core.application.valuestructure.HAPRootStructure;

public class HAPValuePortInteractiveRequest extends HAPValuePortImp{

	private List<HAPDefinitionInteractiveRequestParm> m_requestParms;

	private HAPManualBrickValueStructure m_valueStructureDef;
	
	public HAPValuePortInteractiveRequest(HAPIdValuePort valuePortId, HAPInfoValuePort valuePortInfo, List<HAPDefinitionInteractiveRequestParm> requestParms) {
		super(valuePortId, valuePortInfo);
		this.m_requestParms = requestParms;
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
		for(HAPDefinitionInteractiveRequestParm parm : this.m_requestParms) {
			HAPRootStructure rootStructure = new HAPRootStructure(new HAPElementStructureLeafData(parm.getDataInfo()), parm);
			rootStructure.setName(parm.getName());
			m_valueStructureDef.addRoot(rootStructure);
		}
	}

}
