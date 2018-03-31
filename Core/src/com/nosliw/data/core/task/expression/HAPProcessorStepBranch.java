package com.nosliw.data.core.task.expression;

import java.util.Map;

import com.nosliw.common.utils.HAPProcessContext;
import com.nosliw.data.core.HAPData;
import com.nosliw.data.core.operand.HAPOperandUtility;

public class HAPProcessorStepBranch implements HAPProcessorStep{

	@Override
	public HAPExecutableStep process(HAPDefinitionStep stepDefinition, int index, String name,
			Map<String, HAPData> contextConstants, HAPProcessContext context) {
		HAPDefinitionStepBranch stepDefBranch = (HAPDefinitionStepBranch)stepDefinition;
		
		HAPExecutableStepBranch out = new HAPExecutableStepBranch(stepDefBranch, index, name);
		
		HAPOperandUtility.updateConstantData(out.getExpression(), contextConstants);
		return out;
	}

}
