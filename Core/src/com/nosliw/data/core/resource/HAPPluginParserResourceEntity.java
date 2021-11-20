package com.nosliw.data.core.resource;

import org.json.JSONObject;

import com.nosliw.data.core.complex.HAPDomainDefinitionComplex;
import com.nosliw.data.core.complex.HAPIdEntityInDomain;
import com.nosliw.data.core.component.HAPLocalReferenceBase;

public interface HAPPluginParserResourceEntity {

	/*
	 * parse jsonObj to resource entity into domain
	 * jsonObj json object
	 * domain : domain that entity for resource add to
	 * return : id for the entity
	 */
	HAPIdEntityInDomain parseResourceEntity(JSONObject jsonObj, HAPDomainDefinitionComplex entityDomain, HAPLocalReferenceBase localRefBase);
	
}
