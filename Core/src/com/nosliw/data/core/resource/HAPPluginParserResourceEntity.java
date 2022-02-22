package com.nosliw.data.core.resource;

import org.json.JSONObject;

import com.nosliw.data.core.component.HAPPathLocationBase;
import com.nosliw.data.core.domain.HAPDomainDefinitionEntity;
import com.nosliw.data.core.domain.HAPIdEntityInDomain;

public interface HAPPluginParserResourceEntity {

	/*
	 * parse jsonObj to resource entity into domain
	 * jsonObj json object
	 * domain : domain that entity for resource add to
	 * return : id for the entity
	 */
	HAPIdEntityInDomain parseResourceEntity(JSONObject jsonObj, HAPDomainDefinitionEntity entityDomain, HAPPathLocationBase localRefBase);
	
}
