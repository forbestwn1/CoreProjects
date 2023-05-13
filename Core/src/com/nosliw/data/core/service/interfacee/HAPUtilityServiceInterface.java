package com.nosliw.data.core.service.interfacee;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import com.nosliw.common.interfac.HAPEntityOrReference;
import com.nosliw.common.serialization.HAPSerializationFormat;
import com.nosliw.common.utils.HAPConstantShared;
import com.nosliw.data.core.resource.HAPResourceData;
import com.nosliw.data.core.resource.HAPResourceDependency;
import com.nosliw.data.core.dataassociation.HAPExecutableTask;
import com.nosliw.data.core.dataassociation.HAPIOTask;
import com.nosliw.data.core.resource.HAPFactoryResourceId;
import com.nosliw.data.core.resource.HAPResourceManagerRoot;
import com.nosliw.data.core.runtime.HAPRuntimeInfo;
import com.nosliw.data.core.runtime.js.HAPResourceDataFactory;
import com.nosliw.data.core.service.use.HAPUtilityServiceUse;
import com.nosliw.data.core.valuestructure.HAPContainerStructure;
import com.nosliw.data.core.valuestructure.HAPValueStructureDefinitionFlat;

public class HAPUtilityServiceInterface {

	public static HAPEntityOrReference parseInterface(Object content) {
		HAPEntityOrReference out = null;
		if(content instanceof String) {
			out = HAPFactoryResourceId.newInstance(HAPConstantShared.RUNTIME_RESOURCE_TYPE_SERVICEINTERFACE, content);
		}
		else {
			HAPInfoServiceInterface serviceInterfaceInfo = new HAPInfoServiceInterface();
			serviceInterfaceInfo.buildObject(content, HAPSerializationFormat.JSON);
			out = serviceInterfaceInfo;
		}
		return out;
	}

	//build task io from service interface
	public static HAPIOTask buildIOTaskByInterface(HAPServiceInterface serviceInterface) {
		return new HAPUtilityServiceInterface.HAPIOTaskServiceUse(serviceInterface);
	}
	
	//build service task executable from service interface
	public static HAPExecutableTask buildExecutableTaskByInterface(HAPServiceInterface serviceInterface) {
		return new HAPUtilityServiceInterface.HAPExecutableTaskServiceUse(serviceInterface);
	}
	
	static class HAPExecutableTaskServiceUse extends HAPUtilityServiceInterface.HAPIOTaskServiceUse implements HAPExecutableTask{
		
		public HAPExecutableTaskServiceUse(HAPServiceInterface serviceInterface) {
			super(serviceInterface);
		}
		
		@Override
		public HAPResourceData toResourceData(HAPRuntimeInfo runtimeInfo) {		return HAPResourceDataFactory.createJSValueResourceData("");	}

		@Override
		public List<HAPResourceDependency> getResourceDependency(HAPRuntimeInfo runtimeInfo, HAPResourceManagerRoot resourceManager) {		return null;	}

		@Override
		public String toStringValue(HAPSerializationFormat format) {		return null;		}

		@Override
		public boolean buildObject(Object value, HAPSerializationFormat format) {		return false;	}

	}
	
	static class HAPIOTaskServiceUse implements HAPIOTask{

		HAPServiceInterface m_serviceInterface;
		
		public HAPIOTaskServiceUse(HAPServiceInterface serviceInterface) {
			this.m_serviceInterface = serviceInterface;
		}
		
		@Override
		public HAPContainerStructure getInStructure() {
			return HAPContainerStructure.createDefault(HAPUtilityServiceUse.buildValueStructureFromServiceParms(m_serviceInterface));
		}

		@Override
		public Map<String, HAPContainerStructure> getOutResultStructure() {
			Map<String, HAPContainerStructure> out = new LinkedHashMap<String, HAPContainerStructure>();
			Map<String, HAPValueStructureDefinitionFlat> resultsContext = HAPUtilityServiceUse.buildValueStructureFromResultServiceOutputs(m_serviceInterface);
			for(String resultName : resultsContext.keySet()) {
				out.put(resultName, HAPContainerStructure.createDefault(resultsContext.get(resultName)));
			}
			return out;
		}
	}
}
