package com.nosliw.data.core.service.use;

import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;

import com.nosliw.common.utils.HAPConstant;
import com.nosliw.data.core.component.HAPDefinitionExternalMapping;
import com.nosliw.data.core.component.HAPDefinitionExternalMappingEle;
import com.nosliw.data.core.component.HAPExternalMappingUtility;
import com.nosliw.data.core.criteria.HAPVariableInfo;
import com.nosliw.data.core.script.context.HAPContext;
import com.nosliw.data.core.script.context.HAPContextDefinitionLeafData;
import com.nosliw.data.core.service.interfacee.HAPServiceInterface;
import com.nosliw.data.core.service.interfacee.HAPServiceOutput;
import com.nosliw.data.core.service.interfacee.HAPServiceParm;
import com.nosliw.data.core.service.provide.HAPManagerServiceDefinition;

public class HAPUtilityServiceUse {

	public static HAPContext buildContextFromServiceParms(HAPServiceInterface serviceInterface) {
		return buildContextFromServiceParms(serviceInterface.getParms());
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
	
	//build service provider from external mapping
	public static Set<HAPDefinitionServiceProvider> buildServiceProvider(
			HAPDefinitionExternalMapping resourceExternalMapping,
			Map<String, HAPDefinitionServiceProvider> parent, 
			HAPManagerServiceDefinition serviceDefinitionMan) {
		Set<HAPDefinitionServiceProvider> out = new HashSet<>();
		
		Map<String, HAPDefinitionExternalMappingEle> eleByName = resourceExternalMapping.getMappingByType(HAPConstant.RUNTIME_RESOURCE_TYPE_SERVICE);
		if(eleByName!=null) {
			for(String name : eleByName.keySet()) {
				HAPDefinitionServiceProvider provider = null;
				HAPDefinitionExternalMappingEle ele = eleByName.get(name);
				if(HAPExternalMappingUtility.isOverridenByParent(ele)) {
					//inherited from parent
					if(parent!=null) {
						provider = parent.get(name);
					}
					if(provider==null) {
						//build from ele
						provider = buildServiceProviderFromMappingEle(ele, serviceDefinitionMan);
					}
				}
				else {
					//build from ele
					provider = buildServiceProviderFromMappingEle(ele, serviceDefinitionMan);
				}
				out.add(provider);
			}
		}
		return out;
	}
	
	private static HAPDefinitionServiceProvider buildServiceProviderFromMappingEle(HAPDefinitionExternalMappingEle ele, HAPManagerServiceDefinition serviceDefinitionMan) {
		HAPDefinitionServiceProvider provider = new HAPDefinitionServiceProvider();
		ele.cloneToEntityInfo(provider);
		provider.setServiceId(ele.getId().getId());
		provider.setServiceInterface(serviceDefinitionMan.getDefinition(ele.getId().getId()).getStaticInfo().getInterface());
		return provider;
	}
	
	public static Map<String, HAPDefinitionServiceProvider> buildServiceProvider(
			Map<String, HAPDefinitionServiceProvider> serviceProviders,
			HAPWithServiceProvider withServiceProvider,
			HAPManagerServiceDefinition serviceDefinitionMan) {
		//process service
		//all provider available
		Map<String, HAPDefinitionServiceProvider> allServiceProviders = new LinkedHashMap<String, HAPDefinitionServiceProvider>();
		if(serviceProviders!=null)	allServiceProviders.putAll(serviceProviders);
		allServiceProviders.putAll(withServiceProvider.getServiceProviderDefinitions());
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
