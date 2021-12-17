package com.nosliw.data.core.resource;

import com.nosliw.data.core.component.HAPLocalReferenceBase;
import com.nosliw.data.core.domain.HAPDomainDefinitionEntity;
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
	HAPIdEntityInDomain getResourceEntityBySimpleResourceId(HAPResourceIdSimple resourceId, HAPDomainDefinitionEntity entityDomain);

	/*
	 * get entity for local resource
	 * also build complex entity tree in domain pool
	 * resourceId : simple resource id
	 * localRefBase : base path to get local resource
	 * domain : domain that entity for resource add to
	 * return : id for the entity
	 */
	HAPIdEntityInDomain getResourceEntityByLocalResourceId(HAPResourceIdLocal resourceId, HAPLocalReferenceBase localRefBase, HAPDomainDefinitionEntity entityDomain);

}
