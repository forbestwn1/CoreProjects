package com.nosliw.data.core.resource;

import com.nosliw.data.core.complex.HAPDomainDefinitionComplex;
import com.nosliw.data.core.complex.HAPIdEntityInDomain;
import com.nosliw.data.core.component.HAPLocalReferenceBase;

public interface HAPPluginResourceDefinition {

	String getResourceType();
	
	/*
	 * get entity for resource
	 * also build complex entity tree in domain pool
	 * resourceId : simple resource id
	 * domain : domain that entity for resource add to
	 * return : result (entity id, local resource base)
	 */
	HAPResultSimpleResource getResourceEntityBySimpleResourceId(HAPResourceIdSimple resourceId, HAPDomainDefinitionComplex entityDomain);

	/*
	 * get entity for local resource
	 * also build complex entity tree in domain pool
	 * resourceId : simple resource id
	 * localRefBase : base path to get local resource
	 * domain : domain that entity for resource add to
	 * return : id for the entity
	 */
	HAPIdEntityInDomain getResourceEntityByLocalResourceId(HAPResourceIdLocal resourceId, HAPLocalReferenceBase localRefBase, HAPDomainDefinitionComplex entityDomain);

	/*
	 * 
	 */
	HAPIdEntityInDomain parseResourceEntity(Object content, HAPDomainDefinitionComplex entityDomain, HAPLocalReferenceBase localRefBase);

}
