package com.nosliw.data.core.datasource;

import java.util.Map;

import org.json.JSONObject;

import com.nosliw.common.serialization.HAPSerializationFormat;
import com.nosliw.data.core.HAPData;
import com.nosliw.data.core.task.HAPDefinitionTaskSuite;
import com.nosliw.data.core.task.HAPLogTask;
import com.nosliw.data.core.task.HAPManagerTask;

public class HAPDataSourceFactoryTask implements HAPDataSourceFactory{

	public final static String FACTORY_TYPE = "task";
	
	private HAPManagerTask m_taskManager;
	
	public HAPDataSourceFactoryTask(HAPManagerTask taskManager) {
		this.m_taskManager = taskManager;
	}
	
	@Override
	public HAPExecutableDataSource newDataSource(HAPDefinition dataSourceDefinition) {
		
		JSONObject configJson = (JSONObject)dataSourceDefinition.getConfigure();
		HAPDefinitionTaskSuite taskSuite = new HAPDefinitionTaskSuite(this.m_taskManager);
		taskSuite.buildObject(configJson, HAPSerializationFormat.JSON);
		
		for(String parmName : dataSourceDefinition.getParms().keySet())
		{
			HAPDefinitionParm parmDef = dataSourceDefinition.getParms().get(parmName);
			taskSuite.addVariable(parmName, parmDef.getVaraibleInfo());
		}
		
		HAPExecutableDataSource out = new HAPExecutableDataSource(){
			@Override
			public HAPData getData(Map<String, HAPData> parms) {
				HAPLogTask taskLog = new HAPLogTask();
				return m_taskManager.executeTask("main", taskSuite, parms, taskLog);
			}
		};
		return out;
	}

}
