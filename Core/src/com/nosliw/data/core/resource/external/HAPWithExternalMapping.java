package com.nosliw.data.core.resource.external;

import java.util.Map;

public interface HAPWithExternalMapping {

	HAPDefinitionExternalMapping getExternalMapping();
	
	Map<String, HAPDefinitionExternalMappingEle> getElementsByType(String type);
	
	void mergeBy(HAPWithExternalMapping parent);
	
}
