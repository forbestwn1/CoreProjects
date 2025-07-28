package com.nosliw.core.resource;

import com.nosliw.data.core.domain.HAPDomainEntityDefinitionGlobal;
import com.nosliw.data.core.domain.HAPIdEntityInDomain;

public interface HAPPluginResourceDefinition {

	String getResourceType();
	
	/*
	 * get entity for resource
	 * also build complex entity tree in domain pool
	 * resourceId : simple resource id
	 * domain : domain that entity for resource add to
	 * return : result (entity id, local resource base)
	 */
	HAPIdEntityInDomain getResourceEntityBySimpleResourceId(HAPResourceIdSimple resourceId, HAPDomainEntityDefinitionGlobal globalDomain);

	/*
	 * get entity for local resource
	 * also build complex entity tree in domain pool
	 * resourceId : simple resource id
	 * localRefBase : base path to get local resource
	 * domain : domain that entity for resource add to
	 * return : id for the entity
	 */
	HAPIdEntityInDomain getResourceEntityByLocalResourceId(HAPResourceIdLocal resourceId, HAPDomainEntityDefinitionGlobal globalDomain, String currentDomainId);

	HAPInfoResourceIdNormalize normalizeSimpleResourceId(HAPResourceIdSimple resourceId);
	
	HAPInfoResourceIdNormalize normalizeLocalResourceId(HAPResourceIdLocal resourceId);

}
