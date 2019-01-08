package com.nosliw.data.core.task111.expression;

import java.util.LinkedHashMap;
import java.util.Map;

import com.nosliw.common.serialization.HAPSerializationFormat;
import com.nosliw.common.utils.HAPConstant;
import com.nosliw.common.utils.HAPProcessTracker;
import com.nosliw.data.core.HAPData;
import com.nosliw.data.core.runtime.HAPRuntime;
import com.nosliw.data.core.task111.HAPDefinitionTask;
import com.nosliw.data.core.task111.HAPExecutorTask;
import com.nosliw.data.core.task111.HAPManagerTask;
import com.nosliw.data.core.task111.HAPManagerTaskSpecific;
import com.nosliw.data.core.task111.HAPProcessorTask;

public class HAPManagerTaskExpression implements HAPManagerTaskSpecific{
	private HAPRuntime m_runtime;
	private HAPManagerTask m_taskMan;
	private HAPProcessorTaskExpression m_expressionTaskProcessor;
	private HAPExecutorTaskExpression m_expressionTaskExecutor;
	
	private Map<String, HAPManagerStep> m_stepManagers = new LinkedHashMap<String, HAPManagerStep>();
	
	public HAPManagerTaskExpression(HAPManagerTask taskMan, HAPRuntime runtime) {
		this.m_runtime = runtime;
		this.m_taskMan = taskMan;
		this.m_expressionTaskProcessor = new HAPProcessorTaskExpression(this, this.m_taskMan);
		this.m_expressionTaskExecutor = new HAPExecutorTaskExpression(this, this.m_taskMan);
		registerStepManager(HAPConstant.EXPRESSIONTASK_STEPTYPE_EXPRESSION, new HAPManagerStepExpression(this.m_runtime));
		registerStepManager(HAPConstant.EXPRESSIONTASK_STEPTYPE_LOOP, new HAPManagerStepLoop(this.m_runtime, taskMan));
		registerStepManager(HAPConstant.EXPRESSIONTASK_STEPTYPE_BRANCH, new HAPManagerStepBranch(this.m_runtime));
	}
	
	public void registerStepManager(String type, HAPManagerStep stepManager){		this.m_stepManagers.put(type, stepManager);	}
	public HAPManagerStep getStepManager(String type) {  return this.m_stepManagers.get(type);   }
	
	public HAPExecutableStep processStep(HAPDefinitionStep stepDef, HAPExecutableTaskExpression taskExpressionExe, int index, Map<String, HAPDefinitionTask> contextTaskDefinitions, Map<String, HAPData> constants, HAPProcessTracker processTracker) {
		return this.getStepManager(stepDef.getType()).getStepProcessor().process(stepDef, taskExpressionExe, index, stepDef.getName(), contextTaskDefinitions, constants, processTracker);
	}

	public void postProcessStep(HAPExecutableStep exeDef, HAPDefinitionStep stepDef, HAPExecutableTaskExpression taskExpressionExe, int index, Map<String, HAPDefinitionTask> contextTaskDefinitions, Map<String, HAPData> constants, HAPProcessTracker processTracker) {
		this.getStepManager(stepDef.getType()).getStepProcessor().postProcess(exeDef, stepDef, taskExpressionExe, index, stepDef.getName(), contextTaskDefinitions, constants, processTracker);
	}

	@Override
	public HAPProcessorTask getTaskProcessor() {  return this.m_expressionTaskProcessor; }

	@Override
	public HAPExecutorTask getTaskExecutor() {  return this.m_expressionTaskExecutor; }

	@Override
	public HAPDefinitionTask buildTaskDefinition(Object obj) {
		HAPDefinitionTaskExpression out = new HAPDefinitionTaskExpression(this);
		out.buildObject(obj, HAPSerializationFormat.JSON);
		return out;
	}
	
}
