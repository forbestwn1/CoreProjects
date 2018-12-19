package com.nosliw.data.core.process.activity;

import java.util.Map;

import com.nosliw.common.utils.HAPProcessContext;
import com.nosliw.data.core.HAPData;
import com.nosliw.data.core.expression.HAPVariableInfo;
import com.nosliw.data.core.process.HAPDefinitionActivity;
import com.nosliw.data.core.process.HAPDefinitionProcess;
import com.nosliw.data.core.process.HAPExecutableActivity;
import com.nosliw.data.core.process.HAPExecutableProcess;
import com.nosliw.data.core.process.HAPManagerProcess;
import com.nosliw.data.core.process.HAPProcessorActivity;
import com.nosliw.data.core.script.context.HAPContextGroup;

public class HAPExpressionActivityProcessor implements HAPProcessorActivity{

	@Override
	public HAPExecutableActivity process(HAPDefinitionActivity activityDefinition, HAPExecutableProcess processExe,
			HAPContextGroup context, Map<String, HAPDefinitionProcess> contextProcessDefinitions,
			HAPManagerProcess processManager, HAPProcessContext processContext) {
		HAPExpressionActivityDefinition expActivityDef = (HAPExpressionActivityDefinition)activityDefinition;
		
		HAPExpressionActivityExecutable out = new HAPExpressionActivityExecutable(expActivityDef);
		
		HAPOperandUtility.updateConstantData(out.getOperand(), contextConstants);
		
		return out;
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
