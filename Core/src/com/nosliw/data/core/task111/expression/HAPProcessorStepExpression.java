package com.nosliw.data.core.task111.expression;

import java.util.Map;

import com.nosliw.common.utils.HAPProcessTracker;
import com.nosliw.data.core.HAPData;
import com.nosliw.data.core.operand.HAPOperandUtility;
import com.nosliw.data.core.task111.HAPDefinitionTask;

public class HAPProcessorStepExpression extends HAPProcessorStepImp{

	@Override
	public HAPExecutableStep process(HAPDefinitionStep stepDefinition,
			HAPExecutableTaskExpression taskExpressionExe,
			int index, String name,
			Map<String, HAPDefinitionTask> contextTaskDefinitions, 
			Map<String, HAPData> contextConstants,
			HAPProcessTracker processTracker) {

		HAPDefinitionStepExpression stepDefExp = (HAPDefinitionStepExpression)stepDefinition;
		
		HAPExecutableStepExpression out = new HAPExecutableStepExpression(stepDefExp, index, name);
		
		HAPOperandUtility.updateConstantData(out.getOperand(), contextConstants);
		
		return out;
	}

}
