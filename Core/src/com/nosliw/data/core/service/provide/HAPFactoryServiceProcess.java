package com.nosliw.data.core.service.provide;

import java.util.LinkedHashMap;
import java.util.Map;

import org.json.JSONObject;

import com.nosliw.common.constant.HAPAttribute;
import com.nosliw.common.serialization.HAPSerializationFormat;
import com.nosliw.data.core.data.HAPData;
import com.nosliw.data.core.data.variable.HAPVariableInfo;
import com.nosliw.data.core.process.HAPDefinitionProcessSuite;
import com.nosliw.data.core.process.HAPManagerProcess;
import com.nosliw.data.core.process.HAPRuntimeProcess;
import com.nosliw.data.core.process.util.HAPParserProcessDefinition;
import com.nosliw.data.core.resource.HAPResourceManagerRoot;
import com.nosliw.data.core.script.context.HAPContext;
import com.nosliw.data.core.script.context.HAPContextDefinitionLeafData;
import com.nosliw.data.core.script.context.HAPParentContext;
import com.nosliw.data.core.script.context.data.HAPContextDataFactory;
import com.nosliw.data.core.script.context.dataassociation.HAPDefinitionDataAssociation;
import com.nosliw.data.core.script.context.dataassociation.HAPExecutableWrapperTask;
import com.nosliw.data.core.script.context.dataassociation.HAPParserDataAssociation;
import com.nosliw.data.core.script.context.dataassociation.mirror.HAPDefinitionDataAssociationMirror;
import com.nosliw.data.core.service.interfacee.HAPServiceInterface;
import com.nosliw.data.core.service.interfacee.HAPServiceOutput;
import com.nosliw.data.core.service.interfacee.HAPServiceParm;
import com.nosliw.data.core.service.interfacee.HAPServiceResult;

public class HAPFactoryServiceProcess implements HAPFactoryService{

	public final static String FACTORY_TYPE = "process";
	
	@HAPAttribute
	public static final String SUITE = "suite";

	@HAPAttribute
	public static String INPUTMAPPING = "inputMapping";

	private HAPRuntimeProcess m_processRuntime;
	private HAPManagerProcess m_processMan;
	private HAPResourceManagerRoot m_resourceManager;
	
	public HAPFactoryServiceProcess(HAPRuntimeProcess processRuntime, HAPManagerProcess processMan, HAPResourceManagerRoot resourceManager) {
		this.m_processRuntime = processRuntime;
		this.m_processMan = processMan;
		this.m_resourceManager = resourceManager;
	}
	
	@Override
	public HAPExecutableService newService(HAPDefinitionService dataSourceDefinition) {

		//basic information
		HAPInfoServiceRuntime runtimeInfo = dataSourceDefinition.getRuntimeInfo();
		HAPInfoServiceStatic staticInfo = dataSourceDefinition.getStaticInfo();

		//configuration for service
		JSONObject configJson = (JSONObject)runtimeInfo.getConfigure();
		
		//process suite definition
		HAPDefinitionProcessSuite suite = HAPParserProcessDefinition.parsePocessSuite(configJson.optJSONObject(SUITE), this.m_processMan.getPluginManager());
		suite.setName(staticInfo.getName());

		//input mapping for process
		HAPDefinitionDataAssociation inputMapping;
		JSONObject inputMappingJson = configJson.optJSONObject(INPUTMAPPING);
		if(inputMappingJson!=null) {
			inputMapping = HAPParserDataAssociation.buildDefinitionByJson(inputMappingJson); 
		}
		else {
			inputMapping = new HAPDefinitionDataAssociationMirror();
		}

		//external context from parameter of service
		HAPContext inputExternalContext = new HAPContext();
		HAPServiceInterface serviceInterface = staticInfo.getInterface();
		for(String parmName : serviceInterface.getParmNames()){
			HAPServiceParm parmDef = serviceInterface.getParm(parmName);
			inputExternalContext.addElement(parmName, new HAPContextDefinitionLeafData(HAPVariableInfo.buildVariableInfo((parmDef.getCriteria()))));
		}

		Map<String, HAPParentContext> outputExternalContexts = new LinkedHashMap<String, HAPParentContext>();
		Map<String, HAPServiceResult> serviceResult = serviceInterface.getResults();
		for(String resultName : serviceResult.keySet()) {
			Map<String, HAPServiceOutput> output = serviceResult.get(resultName).getOutput();
			HAPContext outputContext = new HAPContext();
			for(String name : output.keySet()) {
				outputContext.addElement(name, new HAPContextDefinitionLeafData(HAPVariableInfo.buildVariableInfo((output.get(name).getCriteria()))));
			}
			outputExternalContexts.put(resultName, HAPParentContext.createDefault(outputContext));
		}
		
		HAPExecutableWrapperTask processExe = this.m_processMan.getEmbededProcess(
				"main", 
				suite, 
				inputMapping, 
				null,
				HAPParentContext.createDefault(inputExternalContext), 
				outputExternalContexts 
		);
		
		HAPExecutableService out = new HAPExecutableServiceImp(processExe, this.m_resourceManager);
		return out;
	}

	class HAPExecutableServiceImp implements HAPExecutableService{
		private HAPExecutableWrapperTask m_processExe;
		private HAPResourceManagerRoot m_resourceManager;

		public HAPExecutableServiceImp(HAPExecutableWrapperTask processExe, HAPResourceManagerRoot resourceManager) {
			this.m_processExe = processExe;
			this.m_resourceManager = resourceManager;
		}
		
		@Override
		public HAPResultService execute(Map<String, HAPData> parms) {
			JSONObject dataObj = (JSONObject)m_processRuntime.executeEmbededProcess(m_processExe, HAPContextDataFactory.newContextDataFlat(parms), this.m_resourceManager).getData();
//			JSONObject dataObj = (JSONObject)m_processManager.executeProcess("main", suite, parms).getData();
			HAPResultService out = new HAPResultService();
			out.buildObject(dataObj, HAPSerializationFormat.JSON);
			return out;
		}
	}
}
