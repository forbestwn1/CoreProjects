package com.nosliw.data.core.common;

import java.util.Set;

public interface HAPWithConstantDefinition {

	Set<HAPDefinitionConstant> getConstantDefinitions(); 
	
	HAPDefinitionConstant getConstantDefinition(String id);
	
	void addConstantDefinition(HAPDefinitionConstant constantDef);
	
}
