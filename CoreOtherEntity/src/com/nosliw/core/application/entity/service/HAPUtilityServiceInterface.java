package com.nosliw.core.application.entity.service;

import java.util.List;

import com.nosliw.common.interfac.HAPEntityOrReference;
import com.nosliw.common.serialization.HAPSerializationFormat;
import com.nosliw.common.utils.HAPConstantShared;
import com.nosliw.core.application.brick.service.interfacee.HAPBlockServiceInterface;
import com.nosliw.core.dataassociation.HAPExecutableTask;
import com.nosliw.core.dataassociation.HAPIOTask;
import com.nosliw.core.resource.HAPFactoryResourceId;
import com.nosliw.core.resource.HAPManagerResource;
import com.nosliw.core.resource.HAPResourceData;
import com.nosliw.core.resource.HAPResourceDependency;
import com.nosliw.core.resource.imp.js.HAPResourceDataFactory;
import com.nosliw.core.runtime.HAPRuntimeInfo;
import com.nosliw.core.xxx.application1.brick.service.interfacee.HAPBrickServiceInterface1;

public class HAPUtilityServiceInterface {

	public static HAPEntityOrReference parseInterface(Object content) {
		HAPEntityOrReference out = null;
		if(content instanceof String) {
			out = HAPFactoryResourceId.newInstance(HAPConstantShared.RUNTIME_RESOURCE_TYPE_SERVICEINTERFACE, content);
		}
		else {
			HAPBlockServiceInterface serviceInterfaceInfo = new HAPBlockServiceInterface();
			serviceInterfaceInfo.buildObject(content, HAPSerializationFormat.JSON);
			out = serviceInterfaceInfo;
		}
		return out;
	}

	//build task io from service interface
	public static HAPIOTask buildIOTaskByInterface(HAPBrickServiceInterface1 serviceInterface) {
		return new HAPUtilityServiceInterface.HAPIOTaskServiceUse(serviceInterface);
	}
	
	//build service task executable from service interface
	public static HAPExecutableTask buildExecutableTaskByInterface(HAPBrickServiceInterface1 serviceInterface) {
		return new HAPUtilityServiceInterface.HAPExecutableTaskServiceUse(serviceInterface);
	}
	
	static class HAPExecutableTaskServiceUse extends HAPUtilityServiceInterface.HAPIOTaskServiceUse implements HAPExecutableTask{
		
		public HAPExecutableTaskServiceUse(HAPBrickServiceInterface1 serviceInterface) {
			super(serviceInterface);
		}
		
		@Override
		public HAPResourceData toResourceData(HAPRuntimeInfo runtimeInfo) {		return HAPResourceDataFactory.createJSValueResourceData("");	}

		@Override
		public List<HAPResourceDependency> getResourceDependency(HAPRuntimeInfo runtimeInfo, HAPManagerResource resourceManager) {		return null;	}

		@Override
		public String toStringValue(HAPSerializationFormat format) {		return null;		}

		@Override
		public boolean buildObject(Object value, HAPSerializationFormat format) {		return false;	}

	}
	
	static class HAPIOTaskServiceUse implements HAPIOTask{

		HAPBrickServiceInterface1 m_serviceInterface;
		
		public HAPIOTaskServiceUse(HAPBrickServiceInterface1 serviceInterface) {
			this.m_serviceInterface = serviceInterface;
		}
		
//		@Override
//		public HAPContainerStructure getInStructure() {
//			return HAPContainerStructure.createDefault(HAPUtilityServiceUse.buildValueStructureFromServiceParms(m_serviceInterface));
//		}
//
//		@Override
//		public Map<String, HAPContainerStructure> getOutResultStructure() {
//			Map<String, HAPContainerStructure> out = new LinkedHashMap<String, HAPContainerStructure>();
//			Map<String, HAPValueStructureDefinitionFlat> resultsContext = HAPUtilityServiceUse.buildValueStructureFromResultServiceOutputs(m_serviceInterface);
//			for(String resultName : resultsContext.keySet()) {
//				out.put(resultName, HAPContainerStructure.createDefault(resultsContext.get(resultName)));
//			}
//			return out;
//		}
	}
}
