package com.nosliw.data.core.task.expression;

import java.util.Map;

import com.nosliw.common.exception.HAPServiceData;
import com.nosliw.data.core.HAPData;
import com.nosliw.data.core.runtime.HAPRuntime;
import com.nosliw.data.core.runtime.js.rhino.HAPRuntimeTaskExecuteExpressionRhino;

public class HAPExecutorStepExpression implements HAPExecutorStep{

	private HAPRuntime m_runtime;

	public HAPExecutorStepExpression(HAPRuntime runtime) {
		this.m_runtime = runtime;
	}
	
	@Override
	public HAPResultStep execute(HAPExecutableStep step, Map<String, HAPData> parms,
			Map<String, HAPData> referencedData) {

		HAPExecutableStepExpression expStep = (HAPExecutableStepExpression)step;
		HAPRuntimeTaskExecuteExpressionRhino runtimeTask = new HAPRuntimeTaskExecuteExpressionRhino(expStep, parms, referencedData);
		HAPServiceData serviceData = this.m_runtime.executeTaskSync(runtimeTask);
		
		HAPResultStep out = null;
		if(expStep.isExit()) {
			out = HAPResultStep.createExitResult((HAPData)serviceData.getData());
		}
		else {
			out = HAPResultStep.createNextStepResult((HAPData)serviceData.getData(), expStep.getOutputVariable());
		}
		
		return out;
	}

}
