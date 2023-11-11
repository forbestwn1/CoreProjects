package com.nosliw.data.core.domain;

import com.nosliw.common.serialization.HAPSerializationFormat;

//plug in for entity definition in domain
//   how to parse json object for entity
public interface HAPPluginEntityDefinitionInDomain {

	String getEntityType();
	
	//new definition instance
	HAPIdEntityInDomain newInstance(HAPContextParser parserContext);
	
	//parse json to entity, and set entity to existing entity in domain
//	void parseDefinition(HAPIdEntityInDomain entityId, Object obj, HAPContextParser parserContext);
	void parseDefinition(HAPIdEntityInDomain entityId, Object obj, HAPSerializationFormat format, HAPContextParser parserContext);

	
	boolean isComplexEntity();
}
