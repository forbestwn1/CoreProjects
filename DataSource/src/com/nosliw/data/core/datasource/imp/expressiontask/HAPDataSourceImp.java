package com.nosliw.data.core.datasource.imp.expressiontask;

import java.util.Map;

import com.nosliw.data.core.HAPData;
import com.nosliw.data.core.datasource.HAPDataSource;
import com.nosliw.data.core.task.HAPDefinitionTaskSuite;
import com.nosliw.data.core.task.HAPManagerTask;

public class HAPDataSourceImp implements HAPDataSource{

	private HAPManagerTask m_taskManager;
	
	private String m_taskName;
	
	private HAPDefinitionTaskSuite m_suite;
	
	@Override
	public HAPData getData(Map<String, HAPData> parms) {
		return this.m_taskManager.executeTask(m_taskName, m_suite, parms);
	}

}
