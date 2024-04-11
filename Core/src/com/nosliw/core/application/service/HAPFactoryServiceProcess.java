package com.nosliw.core.application.service;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONObject;

import com.nosliw.common.constant.HAPAttribute;
import com.nosliw.common.serialization.HAPSerializationFormat;
import com.nosliw.core.application.brick.interactive.interfacee.HAPResultInInteractiveInterface;
import com.nosliw.core.application.brick.interactive.interfacee.HAPResultOutputInInteractiveInterface;
import com.nosliw.core.application.brick.service.interfacee.HAPBrickServiceInterface1;
import com.nosliw.core.application.brick.service.profile.HAPBrickServiceProfile;
import com.nosliw.core.application.brick.service.profile.HAPInfoServiceStatic;
import com.nosliw.core.application.common.structure.HAPElementStructureLeafData;
import com.nosliw.core.application.common.structure.data.HAPContextDataFactory;
import com.nosliw.core.application.common.variable.HAPVariableDataInfo;
import com.nosliw.core.application.common.variable.HAPVariableInfo;
import com.nosliw.data.core.data.HAPData;
import com.nosliw.data.core.dataassociation.HAPDefinitionDataAssociation;
import com.nosliw.data.core.dataassociation.HAPExecutableWrapperTask;
import com.nosliw.data.core.dataassociation.HAPParserDataAssociation;
import com.nosliw.data.core.dataassociation.mirror.HAPDefinitionDataAssociationMirror;
import com.nosliw.data.core.process1.HAPManagerProcess;
import com.nosliw.data.core.process1.HAPRuntimeProcess;
import com.nosliw.data.core.process1.resource.HAPResourceDefinitionProcessSuite;
import com.nosliw.data.core.process1.util.HAPParserProcessDefinition;
import com.nosliw.data.core.resource.HAPResourceManagerRoot;
import com.nosliw.data.core.valuestructure1.HAPContainerStructure;
import com.nosliw.data.core.valuestructure1.HAPValueStructureDefinitionFlat;

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
	public HAPExecutableService newService(HAPBrickServiceProfile dataSourceDefinition) {

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
		HAPBrickServiceInterface1 serviceInterface = staticInfo.getInterface().getInterface();
		for(HAPVariableInfo parmDef : serviceInterface.getRequestParms()){
			inputExternalContext.addRoot(parmDef.getName(), new HAPElementStructureLeafData(new HAPVariableDataInfo((parmDef.getCriteria()))));
		}

		Map<String, HAPContainerStructure> outputExternalContexts = new LinkedHashMap<String, HAPContainerStructure>();
		Map<String, HAPResultInInteractiveInterface> serviceResult = serviceInterface.getResults();
		for(String resultName : serviceResult.keySet()) {
			List<HAPResultOutputInInteractiveInterface> output = serviceResult.get(resultName).getOutput();
			HAPValueStructureDefinitionFlat outputContext = new HAPValueStructureDefinitionFlat();
			for(HAPResultOutputInInteractiveInterface parm : output) {
				outputContext.addRoot(parm.getName(), new HAPElementStructureLeafData(new HAPVariableDataInfo((parm.getCriteria()))));
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
		public HAPResultInteractive execute(Map<String, HAPData> parms) {
			JSONObject dataObj = (JSONObject)m_processRuntime.executeEmbededProcess(m_processExe, HAPContextDataFactory.newContextDataFlat(parms), this.m_resourceManager).getData();
//			JSONObject dataObj = (JSONObject)m_processManager.executeProcess("main", suite, parms).getData();
			HAPResultInteractive out = new HAPResultInteractive();
			out.buildObject(dataObj, HAPSerializationFormat.JSON);
			return out;
		}
	}
}
