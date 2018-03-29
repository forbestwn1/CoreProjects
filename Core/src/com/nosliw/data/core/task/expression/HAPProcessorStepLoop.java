package com.nosliw.data.core.task.expression;

import java.util.Map;

import com.nosliw.common.utils.HAPProcessContext;
import com.nosliw.data.core.HAPData;
import com.nosliw.data.core.operand.HAPOperandUtility;

public class HAPProcessorStepLoop implements HAPProcessorStep{

	@Override
	public HAPExecutableStep process(HAPDefinitionStep stepDefinition, int index, String name,
			Map<String, HAPData> contextConstants, HAPProcessContext context) {
		
		HAPDefinitionStepLoop stepDefLoop = (HAPDefinitionStepLoop)stepDefinition;
		
		HAPExecutableStepLoop out = new HAPExecutableStepLoop(stepDefLoop, index, name);
		
		HAPOperandUtility.updateConstantData(out.getContainerOperand(), contextConstants);
		HAPOperandUtility.updateConstantData(out.getExecuteOperand(), contextConstants);
		
		return out;
	}

}
