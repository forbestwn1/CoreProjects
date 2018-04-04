package com.nosliw.data.core.task.expression;

import com.nosliw.common.serialization.HAPSerializationFormat;
import com.nosliw.data.core.runtime.HAPRuntime;
import com.nosliw.data.core.task.HAPManagerTask;

public class HAPManagerStepLoop implements HAPManagerStep{

	private HAPProcessorStepLoop m_processor;
	private HAPExecutorStepLoop m_executor;
	
	public HAPManagerStepLoop(HAPRuntime runtime, HAPManagerTask taskManager) {
		this.m_processor = new HAPProcessorStepLoop(taskManager);
		this.m_executor = new HAPExecutorStepLoop(runtime, taskManager);
	}
	
	@Override
	public HAPProcessorStep getStepProcessor() { return this.m_processor; }

	@Override
	public HAPExecutorStep getStepExecutor() {  return this.m_executor;  }

	@Override
	public HAPDefinitionStep buildStepDefinition(Object obj) {
		HAPDefinitionStepLoop out = new HAPDefinitionStepLoop();
		out.buildObject(obj, HAPSerializationFormat.JSON);
		return out;
	}
	
}
