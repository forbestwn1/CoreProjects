package com.nosliw.data.core.task.expression;

import com.nosliw.common.serialization.HAPSerializationFormat;
import com.nosliw.data.core.runtime.HAPRuntime;

public class HAPManagerStepBranch implements HAPManagerStep{

	private HAPProcessorStepBranch m_processor;
	private HAPExecutorStepBranch m_executor;

	public HAPManagerStepBranch(HAPRuntime runtime) {
		this.m_processor = new HAPProcessorStepBranch();
		this.m_executor = new HAPExecutorStepBranch(runtime);
	}
	
	@Override
	public HAPProcessorStep getStepProcessor() {	return this.m_processor;	}

	@Override
	public HAPExecutorStep getStepExecutor() {   return this.m_executor;  }

	@Override
	public HAPDefinitionStep buildStepDefinition(Object obj) {
		HAPDefinitionStepBranch out = new HAPDefinitionStepBranch();
		out.buildObject(obj, HAPSerializationFormat.JSON);
		return out;
	}

}
