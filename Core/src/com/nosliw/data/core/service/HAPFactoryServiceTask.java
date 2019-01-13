package com.nosliw.data.core.service;

import java.util.LinkedHashMap;
import java.util.Map;

import org.json.JSONObject;

import com.nosliw.common.serialization.HAPSerializationFormat;
import com.nosliw.data.core.HAPData;
import com.nosliw.data.core.criteria.HAPVariableInfo;

public class HAPFactoryServiceTask implements HAPFactoryService{

	public final static String FACTORY_TYPE = "task";

	@Override
	public HAPExecutableService newService(HAPDefinitionService serviceDefinition) {
		// TODO Auto-generated method stub
		return null;
	}
	
//	private HAPManagerTask m_taskManager;
	
//	public HAPFactoryServiceTask(HAPManagerTask taskManager) {
//		this.m_taskManager = taskManager;
//	}
//	
//	@Override
//	public HAPExecutableService newService(HAPDefinitionService dataSourceDefinition) {
//		
//		JSONObject configJson = (JSONObject)dataSourceDefinition.getConfigure();
//		HAPDefinitionTaskSuite taskSuite = new HAPDefinitionTaskSuite(this.m_taskManager);
//		taskSuite.buildObject(configJson, HAPSerializationFormat.JSON);
//		taskSuite.setName(dataSourceDefinition.getServiceInfo().getName());
//		
//		for(String parmName : dataSourceDefinition.getServiceInfo().getParms().keySet()){
//			HAPDefinitionServiceParm parmDef = dataSourceDefinition.getServiceInfo().getParms().get(parmName);
//			taskSuite.addVariable(parmName, new HAPVariableInfo(parmDef.getCriteria()));
//		}
//		
//		HAPExecutableService out = new HAPExecutableService(){
//			@Override
//			public Map<String, HAPData> execute(Map<String, HAPData> parms) {
//				HAPLogTask taskLog = new HAPLogTask();
//				Map<String, HAPData> out = new LinkedHashMap<String, HAPData>();
//				out.put("output", m_taskManager.executeTask("main", taskSuite, parms, taskLog));
//				return out;
//			}
//		};
//		return out;
//	}

}
