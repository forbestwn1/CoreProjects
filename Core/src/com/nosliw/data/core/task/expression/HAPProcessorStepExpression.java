package com.nosliw.data.core.task.expression;

import java.util.Map;

import com.nosliw.common.utils.HAPProcessContext;
import com.nosliw.data.core.HAPData;
import com.nosliw.data.core.expression.HAPMatchers;
import com.nosliw.data.core.expression.HAPVariableInfo;
import com.nosliw.data.core.operand.HAPOperandUtility;

public class HAPProcessorStepExpression implements HAPProcessorStep{

	@Override
	public HAPExecutableStep process(HAPDefinitionStep stepDefinition,
			int index, String name,
			Map<String, HAPData> contextConstants,
			HAPProcessContext context) {

		HAPDefinitionStepExpression stepDefExp = (HAPDefinitionStepExpression)stepDefinition;
		
		HAPExecutableStepExpression out = new HAPExecutableStepExpression(stepDefExp, index, name);
		
		HAPOperandUtility.updateConstantData(out.getOperand(), contextConstants);
		
		return out;
	}

}
