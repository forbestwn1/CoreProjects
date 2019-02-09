package com.nosliw.data.core.service.use;

import java.util.LinkedHashMap;
import java.util.Map;

public class HAPDefinitionServiceInEntity {

	//service requirement definition
	private Map<String, HAPDefinitionServiceUse> m_servicesUseDefinition;
	//service provider definition
	private Map<String, HAPDefinitionServiceProvider> m_servicesProviderDefinition;

	public HAPDefinitionServiceInEntity() {
		this.m_servicesUseDefinition = new LinkedHashMap<String, HAPDefinitionServiceUse>();
		this.m_servicesProviderDefinition = new LinkedHashMap<String, HAPDefinitionServiceProvider>();
	}
	
	public void addServiceUseDefinition(HAPDefinitionServiceUse def) {  this.m_servicesUseDefinition.put(def.getName(), def);   }
	public void addServiceProviderDefinition(HAPDefinitionServiceProvider def) {  this.m_servicesProviderDefinition.put(def.getName(), def);   }

	public Map<String, HAPDefinitionServiceUse> getServiceUseDefinitions(){  return this.m_servicesUseDefinition;   }
	public Map<String, HAPDefinitionServiceProvider> getServiceProviderDefinitions(){  return this.m_servicesProviderDefinition;   }
}
