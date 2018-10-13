package com.nosliw.data.core.task111.expression;

import java.util.Map;

import com.nosliw.common.utils.HAPProcessContext;
import com.nosliw.data.core.HAPData;
import com.nosliw.data.core.task111.HAPDefinitionTask;

public abstract class HAPProcessorStepImp implements HAPProcessorStep{

	@Override
	public void postProcess(HAPExecutableStep executableStep, 
			HAPDefinitionStep stepDefinition, 
			HAPExecutableTaskExpression taskExpressionExe,
			int index, String name,
			Map<String, HAPDefinitionTask> contextTaskDefinitions, Map<String, HAPData> contextConstants,
			HAPProcessContext context) {
	}

}
