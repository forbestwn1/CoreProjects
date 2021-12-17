package com.nosliw.data.core.domain;

import org.json.JSONObject;

public class HAPUtilityParserEntity {

	//parse entity into domain
	public static HAPIdEntityInDomain parseEntity(Object entityObj, String entityType, HAPContextParser parserContext) {
		
		HAPIdEntityInDomain out = null;
		
		if(entityObj instanceof JSONObject) {
			out = parserContext.getRuntimeEnvironment().getDomainEntityManager().parseDefinition(entityType, (JSONObject)entityObj, parserContext);
		}
		
		return out;
	}
	
	
	
}
