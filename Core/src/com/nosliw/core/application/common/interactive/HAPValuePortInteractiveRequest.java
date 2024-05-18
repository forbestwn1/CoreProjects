package com.nosliw.core.application.common.interactive;

import java.util.List;

import com.google.common.collect.Lists;
import com.nosliw.common.utils.HAPConstantShared;
import com.nosliw.core.application.common.structure.HAPElementStructureLeafData;
import com.nosliw.core.application.common.valueport.HAPConfigureResolveElementReference;
import com.nosliw.core.application.common.valueport.HAPInfoValuePort;
import com.nosliw.core.application.common.valueport.HAPReferenceValueStructure;
import com.nosliw.core.application.common.valueport.HAPRootStructureInValuePort;
import com.nosliw.core.application.common.valueport.HAPValuePortImp;
import com.nosliw.core.application.common.valueport.HAPValueStructureInValuePort;

public class HAPValuePortInteractiveRequest extends HAPValuePortImp{

	private HAPInteractive m_interactive;
	
	public HAPValuePortInteractiveRequest(HAPInteractive interactive) {
		super(new HAPInfoValuePort(HAPConstantShared.VALUEPORT_TYPE_INTERACTIVE_REQUEST));
		this.getValuePortInfo().setIODirection(HAPConstantShared.IO_DIRECTION_IN);
		this.m_interactive = interactive;
	}

	@Override
	public HAPValueStructureInValuePort getValueStructureDefintion(String valueStructureId) {
		HAPValueStructureInValuePort out = new HAPValueStructureInValuePort();
		for(HAPRequestParmInInteractiveInterface parm : this.m_interactive.getRequestParms()) {
			HAPRootStructureInValuePort root = new HAPRootStructureInValuePort(new HAPElementStructureLeafData(parm.getCriteria()));
			parm.cloneToEntityInfo(root);
			out.addRoot(root);
		}
		return out;
	}

	@Override
	protected List<String> discoverCandidateValueStructure(HAPReferenceValueStructure valueStructureCriteria,
			HAPConfigureResolveElementReference configure) {
		return Lists.asList(HAPConstantShared.NAME_DEFAULT, new String[0]);
	}
}
