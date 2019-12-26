package com.nosliw.data.core.service.use;

import java.util.Map;

public interface HAPWithServiceProvider {

	Map<String, HAPDefinitionServiceProvider> getServiceProviderDefinitions();

	void addServiceProviderDefinition(HAPDefinitionServiceProvider serviceProvider);
}
