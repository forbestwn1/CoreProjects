package com.nosliw.data.core.domain;

import org.json.JSONObject;

public class HAPUtilityParserEntity {

	//parse entity into domain
	public static HAPIdEntityInDomain parseEntity(Object entityObj, String entityType, HAPContextParser parserContext, HAPManagerDomainEntity domainEntityManager) {
		
		HAPIdEntityInDomain out = null;
		
		if(entityObj instanceof JSONObject) {
			out = domainEntityManager.parseDefinition(entityType, (JSONObject)entityObj, parserContext);
		}
		
		return out;
	}
	
	
	
}
