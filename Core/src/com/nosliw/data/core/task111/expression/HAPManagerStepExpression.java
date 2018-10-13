package com.nosliw.data.core.task111.expression;

import com.nosliw.common.serialization.HAPSerializationFormat;
import com.nosliw.data.core.runtime.HAPRuntime;

public class HAPManagerStepExpression implements HAPManagerStep{

	private HAPProcessorStepExpression m_processor;
	private HAPExecutorStepExpression m_executor;
	
	public HAPManagerStepExpression(HAPRuntime runtime) {
		this.m_processor = new HAPProcessorStepExpression();
		this.m_executor = new HAPExecutorStepExpression(runtime);
	}
	
	@Override
	public HAPProcessorStep getStepProcessor() { return this.m_processor; }

	@Override
	public HAPExecutorStep getStepExecutor() {  return this.m_executor;  }

	@Override
	public HAPDefinitionStep buildStepDefinition(Object obj) {
		HAPDefinitionStepExpression out = new HAPDefinitionStepExpression();
		out.buildObject(obj, HAPSerializationFormat.JSON);
		return out;
	}

}
