package com.nosliw.data.core.task.expression;

import java.util.Map;

import com.nosliw.data.core.HAPData;
import com.nosliw.data.core.runtime.HAPRuntime;
import com.nosliw.data.core.runtime.js.rhino.HAPRhinoRuntimeUtility;

public class HAPExecutorStepBranch implements HAPExecutorStep{

	private HAPRuntime m_runtime;

	public HAPExecutorStepBranch(HAPRuntime runtime) {
		this.m_runtime = runtime;
	}
	
	@Override
	public HAPResultStep execute(HAPExecutableStep step, HAPExecutableTaskExpression task, Map<String, HAPData> parms,
			Map<String, HAPData> referencedData) {
		HAPExecutableStepBranch branchStep = (HAPExecutableStepBranch)step;
		
		//get container data
		HAPData expResult = HAPRhinoRuntimeUtility.executeOperandSync(branchStep.getExpression(), parms, referencedData, m_runtime);
		if("true".equals(expResult.getValue()+""))  return branchStep.getTrueResult();
		else return branchStep.getFalseResult();
	}

}
