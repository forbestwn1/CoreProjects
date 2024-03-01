package com.nosliw.data.core.interactive;

import java.util.ArrayList;
import java.util.List;

import com.nosliw.common.utils.HAPConstantShared;
import com.nosliw.data.core.domain.entity.valuestructure.HAPRootStructure;
import com.nosliw.data.core.domain.valueport.HAPReferenceValueStructure;
import com.nosliw.data.core.entity.division.manual.valuestructure.HAPDefinitionEntityValueStructure;
import com.nosliw.data.core.structure.HAPElementStructureLeafData;
import com.nosliw.data.core.structure.reference.HAPContextStructureReference;
import com.nosliw.data.core.structure.reference.HAPInfoValueStructureReference;

public class HAPContextStructureReferenceInteractiveRequest implements HAPContextStructureReference{

	private List<HAPDefinitionInteractiveRequestParm> m_requestParms;

	private HAPDefinitionEntityValueStructure m_valueStructureDef;
	
	public HAPContextStructureReferenceInteractiveRequest(List<HAPDefinitionInteractiveRequestParm> requestParms) {
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
	public HAPDefinitionEntityValueStructure getValueStructureDefintion(String valueStructureId) {
		return this.m_valueStructureDef;
	}

	private void buildValueStructure() {
		this.m_valueStructureDef = new HAPDefinitionEntityValueStructure();
		for(HAPDefinitionInteractiveRequestParm parm : this.m_requestParms) {
			HAPRootStructure rootStructure = new HAPRootStructure(new HAPElementStructureLeafData(parm.getDataInfo()), parm);
			rootStructure.setName(parm.getName());
			m_valueStructureDef.addRoot(rootStructure);
		}
	}
}
