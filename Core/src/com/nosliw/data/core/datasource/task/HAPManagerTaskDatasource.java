package com.nosliw.data.core.datasource.task;

import com.nosliw.data.core.task.HAPDefinitionTask;
import com.nosliw.data.core.task.HAPExecutorTask;
import com.nosliw.data.core.task.HAPManagerTaskSpecific;
import com.nosliw.data.core.task.HAPProcessorTask;

public class HAPManagerTaskDatasource implements HAPManagerTaskSpecific{

	private HAPProcessorTask m_taskProcessor;
	
	private HAPExecutorTask m_taskExecutor;
	
	@Override
	public HAPProcessorTask getTaskProcessor() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public HAPExecutorTask getTaskExecutor() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public HAPDefinitionTask buildTaskDefinition(Object obj) {
		// TODO Auto-generated method stub
		return null;
	}

}
