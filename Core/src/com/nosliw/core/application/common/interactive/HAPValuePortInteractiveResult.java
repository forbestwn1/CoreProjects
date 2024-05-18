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

public class HAPValuePortInteractiveResult extends HAPValuePortImp{

	private HAPInteractive m_interactive;
	
	private String m_resultName;
	
	public HAPValuePortInteractiveResult(HAPInteractive interactive, String resultName) {
		super(new HAPInfoValuePort(HAPConstantShared.VALUEPORT_TYPE_INTERACTIVE_RESULT));
		this.getValuePortInfo().setIODirection(HAPConstantShared.IO_DIRECTION_OUT);
		this.m_interactive = interactive;
		this.m_resultName = resultName;
	}

	@Override
	public HAPValueStructureInValuePort getValueStructureDefintion(String valueStructureId) {
		HAPValueStructureInValuePort out = new HAPValueStructureInValuePort();
		for(HAPResultOutputInInteractiveInterface output : this.m_interactive.getResults().get(this.m_resultName).getOutput()) {
			HAPRootStructureInValuePort root = new HAPRootStructureInValuePort(new HAPElementStructureLeafData(output.getCriteria()));
			output.cloneToEntityInfo(root);
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
