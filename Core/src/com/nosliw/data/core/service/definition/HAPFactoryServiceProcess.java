package com.nosliw.data.core.service.definition;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONObject;

import com.nosliw.common.constant.HAPAttribute;
import com.nosliw.common.serialization.HAPSerializationFormat;
import com.nosliw.data.core.data.HAPData;
import com.nosliw.data.core.data.variable.HAPVariableDataInfo;
import com.nosliw.data.core.dataassociation.HAPDefinitionDataAssociation;
import com.nosliw.data.core.dataassociation.HAPExecutableWrapperTask;
import com.nosliw.data.core.dataassociation.HAPParserDataAssociation;
import com.nosliw.data.core.dataassociation.mirror.HAPDefinitionDataAssociationMirror;
import com.nosliw.data.core.process.HAPManagerProcess;
import com.nosliw.data.core.process.HAPRuntimeProcess;
import com.nosliw.data.core.process.resource.HAPResourceDefinitionProcessSuite;
import com.nosliw.data.core.process.util.HAPParserProcessDefinition;
import com.nosliw.data.core.resource.HAPResourceManagerRoot;
import com.nosliw.data.core.service.interfacee.HAPServiceInterface;
import com.nosliw.data.core.service.interfacee.HAPServiceOutput;
import com.nosliw.data.core.service.interfacee.HAPServiceParm;
import com.nosliw.data.core.service.interfacee.HAPServiceResult;
import com.nosliw.data.core.structure.HAPElementLeafData;
import com.nosliw.data.core.structure.HAPContainerStructure;
import com.nosliw.data.core.structure.data.HAPContextDataFactory;
import com.nosliw.data.core.valuestructure.HAPValueStructureDefinitionFlat;

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
		HAPResourceDefinitionProcessSuite suite = HAPParserProcessDefinition.parsePocessSuite(configJson.optJSONObject(SUITE), this.m_processMan.getPluginManager());
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
		HAPValueStructureDefinitionFlat inputExternalContext = new HAPValueStructureDefinitionFlat();
		HAPServiceInterface serviceInterface = staticInfo.getInterface().getInterface();
		for(HAPServiceParm parmDef : serviceInterface.getParms()){
			inputExternalContext.addRoot(parmDef.getName(), new HAPElementLeafData(new HAPVariableDataInfo((parmDef.getCriteria()))));
		}

		Map<String, HAPContainerStructure> outputExternalContexts = new LinkedHashMap<String, HAPContainerStructure>();
		Map<String, HAPServiceResult> serviceResult = serviceInterface.getResults();
		for(String resultName : serviceResult.keySet()) {
			List<HAPServiceOutput> output = serviceResult.get(resultName).getOutput();
			HAPValueStructureDefinitionFlat outputContext = new HAPValueStructureDefinitionFlat();
			for(HAPServiceOutput parm : output) {
				outputContext.addRoot(parm.getName(), new HAPElementLeafData(new HAPVariableDataInfo((parm.getCriteria()))));
			}
			outputExternalContexts.put(resultName, HAPContainerStructure.createDefault(outputContext));
		}
		
		HAPExecutableWrapperTask processExe = this.m_processMan.getEmbededProcess(
				"main", 
				suite, 
				inputMapping, 
				null,
				HAPContainerStructure.createDefault(inputExternalContext), 
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
