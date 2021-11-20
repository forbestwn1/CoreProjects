package com.nosliw.data.core.resource;

import org.json.JSONObject;

import com.nosliw.common.utils.HAPFileUtility;
import com.nosliw.data.core.complex.HAPDomainDefinitionComplex;
import com.nosliw.data.core.complex.HAPIdEntityInDomain;
import com.nosliw.data.core.component.HAPLocalReferenceBase;

public abstract class HAPPluginResourceDefinitionImp implements HAPPluginResourceDefinition{

	private String m_resourceType;
	
	public HAPPluginResourceDefinitionImp(String resourceType) {
		this.m_resourceType = resourceType;
	}
	
	@Override
	public String getResourceType() {		return this.m_resourceType;	}

	@Override
	public HAPResultSimpleResource getResourceEntityBySimpleResourceId(HAPResourceIdSimple resourceId, HAPDomainDefinitionComplex entityDomain) {
		//get location information
		HAPInfoResourceLocation resourceLocInfo = HAPUtilityResourceId.getResourceLocationInfo(resourceId);
		//parse file
		HAPIdEntityInDomain entityId = this.parseResourceEntity(new JSONObject(HAPFileUtility.readFile(resourceLocInfo.getFiile())), entityDomain, resourceLocInfo.getBasePath());
		return new HAPResultSimpleResource(entityId, resourceLocInfo.getBasePath());
	}

	@Override
	public HAPIdEntityInDomain getResourceEntityByLocalResourceId(HAPResourceIdLocal localResourceId, HAPLocalReferenceBase localRefBase, HAPDomainDefinitionComplex entityDomain) {
		String path = localRefBase.getPath() + localResourceId.getResourceType() + "/" + localResourceId.getName() + ".res";
		HAPIdEntityInDomain out = this.parseResourceEntity(HAPFileUtility.readFile(path), entityDomain, localRefBase);
		return out;
	}

	@Override
	public HAPIdEntityInDomain parseResourceEntity(Object content, HAPDomainDefinitionComplex entityDomain, HAPLocalReferenceBase localRefBase) {
		JSONObject jsonObj = null;
		if(content instanceof JSONObject) jsonObj = (JSONObject)content;
		else if(content instanceof String)  jsonObj = new JSONObject(content);
		
		return this.parseJson(jsonObj, entityDomain, localRefBase);
	}

	abstract public HAPIdEntityInDomain parseJson(JSONObject jsonObj, HAPDomainDefinitionComplex entityDomain, HAPLocalReferenceBase localRefBase);
	
}
