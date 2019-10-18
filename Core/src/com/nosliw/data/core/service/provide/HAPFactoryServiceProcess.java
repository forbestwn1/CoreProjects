package com.nosliw.data.core.service.provide;

import java.util.Map;

import org.json.JSONObject;

import com.nosliw.common.serialization.HAPSerializationFormat;
import com.nosliw.data.core.HAPData;
import com.nosliw.data.core.criteria.HAPVariableInfo;
import com.nosliw.data.core.process.HAPDefinitionProcessSuite;
import com.nosliw.data.core.process.HAPManagerProcess;
import com.nosliw.data.core.process.HAPManagerProcessDefinition;
import com.nosliw.data.core.process.util.HAPParserProcessDefinition;
import com.nosliw.data.core.script.context.HAPContext;
import com.nosliw.data.core.script.context.HAPContextDefinitionLeafData;
import com.nosliw.data.core.service.interfacee.HAPServiceInterface;
import com.nosliw.data.core.service.interfacee.HAPServiceParm;

public class HAPFactoryServiceProcess implements HAPFactoryService{

	public final static String FACTORY_TYPE = "process";
	
	private HAPManagerProcess m_processManager;
	private HAPManagerProcessDefinition m_processDefMan;
	
	public HAPFactoryServiceProcess(HAPManagerProcess processManager, HAPManagerProcessDefinition processDefMan) {
		this.m_processManager = processManager;
		this.m_processDefMan = processDefMan;
	}
	
	@Override
	public HAPExecutableService newService(HAPDefinitionService dataSourceDefinition) {

		HAPInfoServiceRuntime runtimeInfo = dataSourceDefinition.getRuntimeInfo();
		HAPInfoServiceStatic staticInfo = dataSourceDefinition.getStaticInfo();

		JSONObject configJson = (JSONObject)runtimeInfo.getConfigure();
		HAPDefinitionProcessSuite taskSuite = HAPParserProcessDefinition.parsePocessSuite(configJson, this.m_processDefMan.getPluginManager());
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
				JSONObject dataObj = (JSONObject)m_processManager.executeProcess("main", taskSuite, parms).getData();
				HAPResultService out = new HAPResultService();
				out.buildObject(dataObj, HAPSerializationFormat.JSON);
				return out;
			}
		};
		return out;
	}
}
