package com.nosliw.data.core.task111.expression;

import java.util.Map;

import com.nosliw.data.core.HAPData;
import com.nosliw.data.core.task111.HAPLog;
import com.nosliw.data.core.task111.HAPLogTask;

public abstract class HAPExecutorStepImp implements HAPExecutorStep{

	@Override
	public HAPResultStep execute(HAPExecutableStep step, HAPExecutableTaskExpression task, Map<String, HAPData> parms, Map<String, HAPData> referencedData, HAPLogTask taskLog) {
		HAPLogStep stepLog = new HAPLogStep(step, parms, referencedData);
		taskLog.addChild(stepLog);
		HAPResultStep result = this.executeStep(step, task, parms, referencedData, stepLog);
		stepLog.setResult(result);
		return result;
	}

	protected abstract HAPResultStep executeStep(HAPExecutableStep step, HAPExecutableTaskExpression task, Map<String, HAPData> parms, Map<String, HAPData> referencedData, HAPLogStep stepLog);
}
