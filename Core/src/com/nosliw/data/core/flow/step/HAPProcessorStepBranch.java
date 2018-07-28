package com.nosliw.data.core.flow.step;

import java.util.Map;

import com.nosliw.common.utils.HAPProcessContext;
import com.nosliw.data.core.HAPData;
import com.nosliw.data.core.operand.HAPOperandUtility;
import com.nosliw.data.core.task.HAPDefinitionTask;

public class HAPProcessorStepBranch extends HAPProcessorStepImp{

	@Override
	public HAPExecutableStep process(HAPDefinitionStep stepDefinition, 
			HAPExecutableTask taskExpressionExe,
			int index, String name,
			Map<String, HAPDefinitionTask> contextTaskDefinitions, 
			Map<String, HAPData> contextConstants, HAPProcessContext context) {
		HAPDefinitionStepBranch stepDefBranch = (HAPDefinitionStepBranch)stepDefinition;
		
		HAPExecutableStepBranch out = new HAPExecutableStepBranch(stepDefBranch, index, name);
		
		HAPOperandUtility.updateConstantData(out.getExpression(), contextConstants);
		return out;
	}

}
