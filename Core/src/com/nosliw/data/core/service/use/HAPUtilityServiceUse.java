package com.nosliw.data.core.service.use;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONObject;

import com.nosliw.common.serialization.HAPSerializationFormat;
import com.nosliw.common.utils.HAPConstantShared;
import com.nosliw.data.core.component.attachment.HAPAttachment;
import com.nosliw.data.core.component.attachment.HAPAttachmentReference;
import com.nosliw.data.core.component.attachment.HAPContainerAttachment;
import com.nosliw.data.core.component.attachment.HAPUtilityAttachment;
import com.nosliw.data.core.data.variable.HAPVariableDataInfo;
import com.nosliw.data.core.dataassociation.HAPDefinitionDataMappingTask;
import com.nosliw.data.core.runtime.HAPRuntimeEnvironment;
import com.nosliw.data.core.service.definition.HAPDefinitionService;
import com.nosliw.data.core.service.definition.HAPManagerServiceDefinition;
import com.nosliw.data.core.service.interfacee.HAPServiceInterface;
import com.nosliw.data.core.service.interfacee.HAPServiceOutput;
import com.nosliw.data.core.service.interfacee.HAPServiceParm;
import com.nosliw.data.core.structure.HAPElementLeafData;
import com.nosliw.data.core.valuestructure.HAPValueStructureDefinitionFlat;

public class HAPUtilityServiceUse {

	public static HAPInfoServiceProvider parseServiceAttachment(HAPAttachment providerAttachment, HAPRuntimeEnvironment runtimeEnv) {
		HAPDefinitionService serviceDef = (HAPDefinitionService)HAPUtilityAttachment.getResourceDefinition(providerAttachment, runtimeEnv.getResourceDefinitionManager());

		HAPDefinitionDataMappingTask dataMapping = null;
		Object adaptor = providerAttachment.getAdaptor();
		if(adaptor!=null) {
			JSONObject dataMappingObj = ((JSONObject)adaptor).optJSONObject(HAPInfoServiceProvider.DATAMAPPING);
			dataMapping = new HAPDefinitionDataMappingTask();
			dataMapping.buildObject(dataMappingObj, HAPSerializationFormat.JSON);
		}
		
		return new HAPInfoServiceProvider(providerAttachment, serviceDef, dataMapping);
	}
	
	public static HAPValueStructureDefinitionFlat buildContextFromServiceParms(HAPServiceInterface serviceInterface) {
		HAPValueStructureDefinitionFlat out = new HAPValueStructureDefinitionFlat();
		for(HAPServiceParm parm : serviceInterface.getParms()) {
			out.addRoot(parm.getName(), new HAPElementLeafData(new HAPVariableDataInfo(parm.getCriteria())));
		}
		return out;
	}
	
	public static HAPValueStructureDefinitionFlat buildContextFromServiceParms(Map<String, HAPServiceParm> parms) {
		HAPValueStructureDefinitionFlat out = new HAPValueStructureDefinitionFlat();
		for(String parm : parms.keySet()) {
			out.addRoot(parm, new HAPElementLeafData(new HAPVariableDataInfo(parms.get(parm).getCriteria())));
		}
		return out;
	}

	public static Map<String, HAPValueStructureDefinitionFlat> buildContextFromResultServiceOutputs(HAPServiceInterface serviceInterface) {
		Map<String, HAPValueStructureDefinitionFlat> out = new LinkedHashMap<String, HAPValueStructureDefinitionFlat>();
		for(String resultName : serviceInterface.getResults().keySet()) {
			out.put(resultName, buildContextFromServiceOutputs(serviceInterface.getResultOutput(resultName)));
		}
		return out;
	}

	public static HAPValueStructureDefinitionFlat buildContextFromResultServiceOutputs(HAPServiceInterface serviceInterface, String result) {
		return buildContextFromServiceOutputs(serviceInterface.getResultOutput(result));
	}
	
	public static HAPValueStructureDefinitionFlat buildContextFromServiceOutputs(List<HAPServiceOutput> serviceOutput) {
		HAPValueStructureDefinitionFlat out = new HAPValueStructureDefinitionFlat();
		for(HAPServiceOutput outParm : serviceOutput) {
			out.addRoot(outParm.getName(), new HAPElementLeafData(new HAPVariableDataInfo(outParm.getCriteria())));
		}
		return out;
	}
	
	//build service provider from attachment
	public static Map<String, HAPDefinitionServiceProvider> buildServiceProvider(
			HAPContainerAttachment attachment,
			Map<String, HAPDefinitionServiceProvider> parent, 
			HAPManagerServiceDefinition serviceDefinitionMan) {
		Map<String, HAPDefinitionServiceProvider> out = new LinkedHashMap<>();
		if(parent!=null)   out.putAll(parent);
		
		Map<String, HAPAttachment> eleByName = attachment.getAttachmentByType(HAPConstantShared.RUNTIME_RESOURCE_TYPE_SERVICE);
		if(eleByName!=null) {
			for(String name : eleByName.keySet()) {
				HAPDefinitionServiceProvider provider = null;
				Object eleObj = eleByName.get(name);
				HAPAttachmentReference ele = (HAPAttachmentReference)eleObj;
				//build from ele
//				provider = buildServiceProviderFromAttachment(ele, serviceDefinitionMan);
//				if(HAPAttachmentUtility.isOverridenByParent(ele)) {
//					//inherited from parent
//					if(parent!=null) {
//						provider = parent.get(name);
//					}
//					if(provider==null) {
//						//build from ele
//						provider = buildServiceProviderFromAttachment(ele, serviceDefinitionMan);
//					}
//				}
//				else {
//					//build from ele
//					provider = buildServiceProviderFromAttachment(ele, serviceDefinitionMan);
//				}
//				out.put(provider.getName(), provider);
			}
		}
		return out;
	}
	
//	private static HAPDefinitionServiceProvider buildServiceProviderFromAttachment(HAPAttachmentReference ele, HAPManagerServiceDefinition serviceDefinitionMan) {
//		HAPDefinitionServiceProvider provider = new HAPDefinitionServiceProvider();
//		ele.cloneToEntityInfo(provider);
//		String serviceId = ele.getReferenceId().getIdLiterate();
//		provider.setServiceId(serviceId);
//		provider.setServiceInterface(serviceDefinitionMan.getDefinition(serviceId).getStaticInfo().getInterface());
//		return provider;
//	}
	
//	public static Map<String, HAPDefinitionServiceProvider> buildServiceProvider(
//			Map<String, HAPDefinitionServiceProvider> serviceProviders,
//			HAPWithServiceUse withServiceUse,
//			HAPManagerServiceDefinition serviceDefinitionMan) {
//		//process service
//		//all provider available
//		Map<String, HAPDefinitionServiceProvider> allServiceProviders = new LinkedHashMap<String, HAPDefinitionServiceProvider>();
//		if(serviceProviders!=null)	allServiceProviders.putAll(serviceProviders);
//		allServiceProviders.putAll(withServiceUse.getServiceProviderDefinitions());
//		//make sure all provider has interface info
//		for(String name : allServiceProviders.keySet()) {
//			HAPDefinitionServiceProvider provider = allServiceProviders.get(name);
//			if(provider.getServiceInterface()==null) {
//				provider.setServiceInterface(serviceDefinitionMan.getDefinition(provider.getServiceId()).getStaticInfo().getInterface());
//			}
//		}
//		return allServiceProviders;
//	}
}
