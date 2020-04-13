package com.nosliw.data.core.common;

import java.util.Map;

public interface HAPWithConstantDefinition {

	Map<String, HAPDefinitionConstant> getConstantDefinitions(); 
	
	HAPDefinitionConstant getConstantDefinition(String name);
	
	void addConstantDefinition(HAPDefinitionConstant constantDef);
	
}
