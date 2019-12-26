package com.nosliw.data.core.component;

import java.util.Map;

public interface HAPWithExternalMapping {

	public static final String EXTERNALMAPPING = "external";
	
	HAPDefinitionExternalMapping getExternalMapping();
	
	Map<String, HAPDefinitionExternalMappingEle> getElementsByType(String type);
	
	void mergeBy(HAPWithExternalMapping parent, String mode);
	
}
