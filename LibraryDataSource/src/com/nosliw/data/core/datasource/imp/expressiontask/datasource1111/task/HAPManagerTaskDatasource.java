package com.nosliw.data.core.datasource.task;

import com.nosliw.common.serialization.HAPSerializationFormat;
import com.nosliw.core.datasource.HAPDataSourceManager;
import com.nosliw.data.core.datasource.HAPDataSourceDefinitionManager;
import com.nosliw.data.core.runtime.HAPRuntime;
import com.nosliw.data.core.task111.HAPDefinitionTask;
import com.nosliw.data.core.task111.HAPExecutorTask;
import com.nosliw.data.core.task111.HAPManagerTaskSpecific;
import com.nosliw.data.core.task111.HAPProcessorTask;

public class HAPManagerTaskDatasource implements HAPManagerTaskSpecific{

	private HAPProcessorTask m_taskProcessor;
	
	private HAPExecutorTask m_taskExecutor;
	
	private HAPDataSourceDefinitionManager m_dataSourceDefMan;
	private HAPDataSourceManager m_dataSourceManager;
	private HAPRuntime m_runtime;
	
	public HAPManagerTaskDatasource(HAPDataSourceDefinitionManager dataSourceDefMan, HAPDataSourceManager dataSourceManager, HAPRuntime runtime) {
		this.m_runtime = runtime;
		this.m_dataSourceDefMan = dataSourceDefMan;
		this.m_dataSourceManager = dataSourceManager;
		this.m_taskProcessor = new HAPProcessorTaskDataSource(this.m_dataSourceDefMan);
		this.m_taskExecutor = new HAPExecutorTaskDataSource(this.m_dataSourceManager, this.m_runtime);
	}
	
	@Override
	public HAPProcessorTask getTaskProcessor() {  return this.m_taskProcessor; }

	@Override
	public HAPExecutorTask getTaskExecutor() {  return this.m_taskExecutor;  }

	@Override
	public HAPDefinitionTask buildTaskDefinition(Object obj) {
		HAPDefinitionTaskDataSource out = new HAPDefinitionTaskDataSource();
		out.buildObject(obj, HAPSerializationFormat.JSON);
		return out;
	}

}
