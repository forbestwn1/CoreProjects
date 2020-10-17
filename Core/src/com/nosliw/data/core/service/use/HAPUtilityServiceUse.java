package com.nosliw.data.core.service.use;

import java.util.LinkedHashMap;
import java.util.Map;

import com.nosliw.common.utils.HAPConstant;
import com.nosliw.data.core.component.attachment.HAPAttachment;
import com.nosliw.data.core.component.attachment.HAPAttachmentContainer;
import com.nosliw.data.core.component.attachment.HAPAttachmentReference;
import com.nosliw.data.core.data.variable.HAPVariableInfo;
import com.nosliw.data.core.script.context.HAPContext;
import com.nosliw.data.core.script.context.HAPContextDefinitionLeafData;
import com.nosliw.data.core.service.interfacee.HAPServiceInterface;
import com.nosliw.data.core.service.interfacee.HAPServiceOutput;
import com.nosliw.data.core.service.interfacee.HAPServiceParm;
import com.nosliw.data.core.service.provide.HAPManagerServiceDefinition;

public class HAPUtilityServiceUse {

	public static HAPContext buildContextFromServiceParms(HAPServiceInterface serviceInterface) {
		HAPContext out = new HAPContext();
		for(String parm : serviceInterface.getParmNames()) {
			out.addElement(parm, new HAPContextDefinitionLeafData(HAPVariableInfo.buildVariableInfo(serviceInterface.getParm(parm).getCriteria())));
		}
		return out;
	}
	
	public static HAPContext buildContextFromServiceParms(Map<String, HAPServiceParm> parms) {
		HAPContext out = new HAPContext();
		for(String parm : parms.keySet()) {
			out.addElement(parm, new HAPContextDefinitionLeafData(HAPVariableInfo.buildVariableInfo(parms.get(parm).getCriteria())));
		}
		return out;
	}

	public static Map<String, HAPContext> buildContextFromResultServiceOutputs(HAPServiceInterface serviceInterface) {
		Map<String, HAPContext> out = new LinkedHashMap<String, HAPContext>();
		for(String resultName : serviceInterface.getResults().keySet()) {
			out.put(resultName, buildContextFromServiceOutputs(serviceInterface.getResultOutput(resultName)));
		}
		return out;
	}

	public static HAPContext buildContextFromResultServiceOutputs(HAPServiceInterface serviceInterface, String result) {
		return buildContextFromServiceOutputs(serviceInterface.getResultOutput(result));
	}
	
	public static HAPContext buildContextFromServiceOutputs(Map<String, HAPServiceOutput> serviceOutput) {
		HAPContext out = new HAPContext();
		for(String outParm : serviceOutput.keySet()) {
			out.addElement(outParm, new HAPContextDefinitionLeafData(HAPVariableInfo.buildVariableInfo(serviceOutput.get(outParm).getCriteria())));
		}
		return out;
	}
	
	//build service provider from attachment
	public static Map<String, HAPDefinitionServiceProvider> buildServiceProvider(
			HAPAttachmentContainer attachment,
			Map<String, HAPDefinitionServiceProvider> parent, 
			HAPManagerServiceDefinition serviceDefinitionMan) {
		Map<String, HAPDefinitionServiceProvider> out = new LinkedHashMap<>();
		if(parent!=null)   out.putAll(parent);
		
		Map<String, HAPAttachment> eleByName = attachment.getAttachmentByType(HAPConstant.RUNTIME_RESOURCE_TYPE_SERVICE);
		if(eleByName!=null) {
			for(String name : eleByName.keySet()) {
				HAPDefinitionServiceProvider provider = null;
				HAPAttachmentReference ele = (HAPAttachmentReference)eleByName.get(name);
				//build from ele
				provider = buildServiceProviderFromAttachment(ele, serviceDefinitionMan);
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
				out.put(provider.getName(), provider);
			}
		}
		return out;
	}
	
	private static HAPDefinitionServiceProvider buildServiceProviderFromAttachment(HAPAttachmentReference ele, HAPManagerServiceDefinition serviceDefinitionMan) {
		HAPDefinitionServiceProvider provider = new HAPDefinitionServiceProvider();
		ele.cloneToEntityInfo(provider);
		String serviceId = ele.getReferenceId().getIdLiterate();
		provider.setServiceId(serviceId);
		provider.setServiceInterface(serviceDefinitionMan.getDefinition(serviceId).getStaticInfo().getInterface());
		return provider;
	}
	
	public static Map<String, HAPDefinitionServiceProvider> buildServiceProvider(
			Map<String, HAPDefinitionServiceProvider> serviceProviders,
			HAPWithServiceUse withServiceUse,
			HAPManagerServiceDefinition serviceDefinitionMan) {
		//process service
		//all provider available
		Map<String, HAPDefinitionServiceProvider> allServiceProviders = new LinkedHashMap<String, HAPDefinitionServiceProvider>();
		if(serviceProviders!=null)	allServiceProviders.putAll(serviceProviders);
		allServiceProviders.putAll(withServiceUse.getServiceProviderDefinitions());
		//make sure all provider has interface info
		for(String name : allServiceProviders.keySet()) {
			HAPDefinitionServiceProvider provider = allServiceProviders.get(name);
			if(provider.getServiceInterface()==null) {
				provider.setServiceInterface(serviceDefinitionMan.getDefinition(provider.getServiceId()).getStaticInfo().getInterface());
			}
		}
		return allServiceProviders;
	}
}
