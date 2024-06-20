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

public class HAPValuePortInteractiveExpressionResult extends HAPValuePortImp{

	private HAPResultElementInInteractiveExpression m_expressionResult;
	
	public HAPValuePortInteractiveExpressionResult(HAPResultElementInInteractiveExpression expressionResult, String ioDirection) {
		super(new HAPInfoValuePort(HAPConstantShared.VALUEPORT_TYPE_INTERACTIVE_RESULT, ioDirection));
		this.m_expressionResult = expressionResult;
	}

	@Override
	public HAPValueStructureInValuePort getValueStructureDefintion(String valueStructureId) {
		HAPValueStructureInValuePort out = new HAPValueStructureInValuePort();
		
		HAPRootStructureInValuePort root = new HAPRootStructureInValuePort(new HAPElementStructureLeafData(this.m_expressionResult.getDataCriteria()));
		root.setName(HAPConstantShared.NAME_ROOT_RESULT);
		out.addRoot(root);
		
		return out;
	}

	@Override
	protected List<String> discoverCandidateValueStructure(HAPReferenceValueStructure valueStructureCriteria,
			HAPConfigureResolveElementReference configure) {
		return Lists.asList(HAPConstantShared.NAME_DEFAULT, new String[0]);
	}
}
