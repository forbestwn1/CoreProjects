package com.nosliw.data.core.domain;

//plug in for entity definition in domain
//   how to parse json object for entity
public interface HAPPluginEntityDefinitionInDomain {

	String getEntityType();
	
	//new definition instance
	HAPIdEntityInDomain newInstance(HAPContextParser parserContext);
	
	//parse json to entity, and set entity to existing entity in domain
	void parseDefinition(HAPIdEntityInDomain entityId, Object obj, HAPContextParser parserContext);

	boolean isComplexEntity();
}
