package com.nosliw.data.core.service.use;

import java.util.Map;

import com.nosliw.common.constant.HAPAttribute;

public interface HAPWithServiceUse {

	@HAPAttribute
	public static String SERVICE = "service";
	
	Map<String, HAPDefinitionServiceProvider> getServiceProviderDefinitions();

	void addServiceProviderDefinition(HAPDefinitionServiceProvider serviceProvider);

	Map<String, HAPDefinitionServiceUse> getServiceUseDefinitions();
	
	void addServiceUseDefinition(HAPDefinitionServiceUse def);

}
