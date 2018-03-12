package com.nosliw.data.core.datasource.imp.expressiontask;

import org.json.JSONObject;

import com.nosliw.common.serialization.HAPSerializationFormat;
import com.nosliw.data.core.datasource.HAPDataSource;
import com.nosliw.data.core.datasource.HAPDataSourceFactory;
import com.nosliw.data.core.datasource.HAPDefinition;
import com.nosliw.data.core.task.HAPDefinitionTaskSuite;
import com.nosliw.data.core.task.HAPManagerTask;

public class HAPDataSourceFactoryTask implements HAPDataSourceFactory{

	private HAPManagerTask m_taskManager;
	
	@Override
	public HAPDataSource newDataSource(HAPDefinition dataSourceDefinition) {
		
		JSONObject configJson = (JSONObject)dataSourceDefinition.getConfigure();
		HAPDefinitionTaskSuite taskSuite = new HAPDefinitionTaskSuite();
		taskSuite.buildObject(taskSuite, HAPSerializationFormat.JSON);
		
		
		
		
		return null;
	}

}
