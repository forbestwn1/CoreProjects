package com.nosliw.data.core.process.activity;

import java.util.Map;

import com.nosliw.common.utils.HAPProcessContext;
import com.nosliw.data.core.HAPData;
import com.nosliw.data.core.expression.HAPVariableInfo;
import com.nosliw.data.core.operand.HAPOperandUtility;
import com.nosliw.data.core.process.HAPDefinitionActivity;
import com.nosliw.data.core.process.HAPDefinitionProcess;
import com.nosliw.data.core.process.HAPExecutableActivity;
import com.nosliw.data.core.process.HAPExecutableProcess;
import com.nosliw.data.core.process.HAPProcessorActivity;
import com.nosliw.data.core.task111.HAPDefinitionTask;

public class HAPEndActivityProcessor implements HAPProcessorActivity{

	@Override
	public HAPExecutableActivity process(HAPDefinitionActivity activityDefinition, HAPExecutableProcess processExe,
			Map<String, HAPVariableInfo> variables, String name,
			Map<String, HAPDefinitionProcess> contextProcessDefinitions, HAPProcessContext context) {

		HAPExpressionActivityDefinition expressionActivity = (HAPExpressionActivityDefinition)activityDefinition;

		
		return null;
	}

	
	@Override
	public void postProcess(HAPExecutableActivity executableStep, HAPDefinitionActivity activityDefinition,
			HAPExecutableProcess taskExpressionExe, int index, String name,
			Map<String, HAPDefinitionProcess> contextTaskDefinitions, Map<String, HAPData> contextConstants,
			HAPProcessContext context) {

		
	}
/*
extends HAPProcessorStepImp{

	@Override
	public HAPExecutableStep process(HAPDefinitionStep stepDefinition,
			HAPExecutableTask taskExpressionExe,
			int index, String name,
			Map<String, HAPDefinitionTask> contextTaskDefinitions, 
			Map<String, HAPData> contextConstants,
			HAPProcessContext context) {

		HAPDefinitionStepExpression stepDefExp = (HAPDefinitionStepExpression)stepDefinition;
		
		HAPExecutableStepExpression out = new HAPExecutableStepExpression(stepDefExp, index, name);
		
		HAPOperandUtility.updateConstantData(out.getOperand(), contextConstants);
		
		return out;
	}
*/

}
