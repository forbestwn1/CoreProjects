package com.nosliw.data.core.service.provide;

import java.util.Map;

import org.json.JSONObject;

import com.nosliw.common.serialization.HAPSerializationFormat;
import com.nosliw.data.core.HAPData;
import com.nosliw.data.core.criteria.HAPVariableInfo;
import com.nosliw.data.core.process.HAPDefinitionProcessSuite;
import com.nosliw.data.core.process.HAPManagerProcess;
import com.nosliw.data.core.script.context.HAPContext;
import com.nosliw.data.core.script.context.HAPContextDefinitionLeafData;
import com.nosliw.data.core.service.interfacee.HAPServiceInterface;
import com.nosliw.data.core.service.interfacee.HAPServiceParm;

public class HAPFactoryServiceTask implements HAPFactoryService{

	public final static String FACTORY_TYPE = "process";
	
	private HAPManagerProcess m_processManager;
	
	@Override
	public HAPExecutableService newService(HAPDefinitionService dataSourceDefinition) {

		HAPInfoServiceRuntime runtimeInfo = dataSourceDefinition.getRuntimeInfo();
		HAPInfoServiceStatic staticInfo = dataSourceDefinition.getStaticInfo();

		JSONObject configJson = (JSONObject)runtimeInfo.getConfigure();
		HAPDefinitionProcessSuite taskSuite = new HAPDefinitionProcessSuite();
		taskSuite.buildObject(configJson, HAPSerializationFormat.JSON);
		taskSuite.setName(staticInfo.getName());
		
		HAPContext processExternalContext = new HAPContext();
		HAPServiceInterface serviceInterface = staticInfo.getInterface();
		for(String parmName : serviceInterface.getParms().keySet()){
			HAPServiceParm parmDef = serviceInterface.getParms().get(parmName);
			processExternalContext.addElement(parmName, new HAPContextDefinitionLeafData(HAPVariableInfo.buildVariableInfo((parmDef.getCriteria()))));
		}
		
		HAPExecutableService out = new HAPExecutableService(){
			@Override
			public HAPResultService execute(Map<String, HAPData> parms) {
				return (HAPResultService)m_processManager.executeProcess("main", taskSuite, parms).getData();
			}
		};
		return out;
	}
}
