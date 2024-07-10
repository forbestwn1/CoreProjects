package com.nosliw.data.core.service.use;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONObject;

import com.nosliw.common.serialization.HAPSerializationFormat;
import com.nosliw.common.utils.HAPConstantShared;
import com.nosliw.core.application.brick.service.interfacee.HAPBrickServiceInterface1;
import com.nosliw.core.application.brick.service.profile.HAPBlockServiceProfile;
import com.nosliw.core.application.common.interactive.HAPResultElementInInteractiveTask;
import com.nosliw.core.application.common.structure.HAPElementStructureLeafData;
import com.nosliw.core.application.common.variable.HAPVariableDataInfo;
import com.nosliw.core.application.common.variable.HAPVariableDefinition;
import com.nosliw.core.application.division.manual.brick.adapter.dataassociationfortask.HAPDefinitionGroupDataAssociationForTask;
import com.nosliw.core.application.service.HAPManagerServiceDefinition;
import com.nosliw.data.core.domain.entity.attachment.HAPAttachment;
import com.nosliw.data.core.domain.entity.attachment.HAPDefinitionEntityContainerAttachment;
import com.nosliw.data.core.domain.entity.attachment1.HAPAttachmentReference;
import com.nosliw.data.core.domain.entity.attachment1.HAPUtilityAttachment;
import com.nosliw.data.core.runtime.HAPRuntimeEnvironment;
import com.nosliw.data.core.valuestructure1.HAPValueStructureDefinitionFlat;

public class HAPUtilityServiceUse {

	public static HAPInfoServiceProvider parseServiceAttachment(HAPAttachment providerAttachment, HAPRuntimeEnvironment runtimeEnv) {
		HAPBlockServiceProfile serviceDef = (HAPBlockServiceProfile)HAPUtilityAttachment.getResourceDefinition(providerAttachment, runtimeEnv.getResourceDefinitionManager());

		HAPDefinitionGroupDataAssociationForTask dataMapping = null;
		Object adaptor = providerAttachment.getAdaptor();
		if(adaptor!=null) {
			JSONObject dataMappingObj = ((JSONObject)adaptor).optJSONObject(HAPInfoServiceProvider.DATAMAPPING);
			dataMapping = new HAPDefinitionGroupDataAssociationForTask();
			dataMapping.buildObject(dataMappingObj, HAPSerializationFormat.JSON);
		}
		
		return new HAPInfoServiceProvider(providerAttachment, serviceDef, dataMapping);
	}
	
	public static HAPValueStructureDefinitionFlat buildValueStructureFromServiceParms(HAPBrickServiceInterface1 serviceInterface) {
		HAPValueStructureDefinitionFlat out = new HAPValueStructureDefinitionFlat();
		for(HAPVariableDefinition parm : serviceInterface.getRequestParms()) {
			out.addRoot(parm.getName(), new HAPElementStructureLeafData(new HAPVariableDataInfo(parm.getCriteria())));
		}
		return out;
	}
	
	public static HAPValueStructureDefinitionFlat buildValueStructureFromServiceParms(Map<String, HAPVariableDefinition> parms) {
		HAPValueStructureDefinitionFlat out = new HAPValueStructureDefinitionFlat();
		for(String parm : parms.keySet()) {
			out.addRoot(parm, new HAPElementStructureLeafData(new HAPVariableDataInfo(parms.get(parm).getCriteria())));
		}
		return out;
	}

	public static Map<String, HAPValueStructureDefinitionFlat> buildValueStructureFromResultServiceOutputs(HAPBrickServiceInterface1 serviceInterface) {
		Map<String, HAPValueStructureDefinitionFlat> out = new LinkedHashMap<String, HAPValueStructureDefinitionFlat>();
		for(String resultName : serviceInterface.getResults().keySet()) {
			out.put(resultName, buildValueStructureFromServiceOutputs(serviceInterface.getResultOutput(resultName)));
		}
		return out;
	}

	public static HAPValueStructureDefinitionFlat buildValueStructureFromResultServiceOutputs(HAPBrickServiceInterface1 serviceInterface, String result) {
		return buildValueStructureFromServiceOutputs(serviceInterface.getResultOutput(result));
	}
	
	public static HAPValueStructureDefinitionFlat buildValueStructureFromServiceOutputs(List<HAPResultElementInInteractiveTask> serviceOutput) {
		HAPValueStructureDefinitionFlat out = new HAPValueStructureDefinitionFlat();
		for(HAPResultElementInInteractiveTask outParm : serviceOutput) {
			out.addRoot(outParm.getName(), new HAPElementStructureLeafData(new HAPVariableDataInfo(outParm.getCriteria())));
		}
		return out;
	}
	
	//build service provider from attachment
	public static Map<String, HAPDefinitionServiceProvider> buildServiceProvider(
			HAPDefinitionEntityContainerAttachment attachment,
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
