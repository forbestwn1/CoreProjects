package com.nosliw.data.core.domain;

import org.json.JSONObject;

//plug in for entity definition in domain
//   how to parse json object for entity
public interface HAPPluginEntityDefinitionInDomain {

	String getEntityType();
	
	//parse json to entity
	HAPIdEntityInDomain parseDefinition(JSONObject jsonObj, HAPContextParser parserContext);

}
