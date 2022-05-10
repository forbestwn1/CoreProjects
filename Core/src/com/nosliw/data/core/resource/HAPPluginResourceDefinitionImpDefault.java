package com.nosliw.data.core.resource;

import org.json.JSONObject;

import com.nosliw.common.serialization.HAPJsonUtility;
import com.nosliw.common.utils.HAPFileUtility;
import com.nosliw.data.core.component.HAPPathLocationBase;
import com.nosliw.data.core.domain.HAPContextParser;
import com.nosliw.data.core.domain.HAPDomainEntityDefinitionGlobal;
import com.nosliw.data.core.domain.HAPDomainEntityDefinitionSimpleResource;
import com.nosliw.data.core.domain.HAPIdEntityInDomain;
import com.nosliw.data.core.domain.HAPUtilityDomain;
import com.nosliw.data.core.domain.HAPUtilityParserEntity;
import com.nosliw.data.core.runtime.HAPRuntimeEnvironment;

public class HAPPluginResourceDefinitionImpDefault implements HAPPluginResourceDefinition{

	private String m_resourceType;
	
	private HAPRuntimeEnvironment m_runtimeEnv;
	
	public HAPPluginResourceDefinitionImpDefault(String resourceType, HAPRuntimeEnvironment runtimeEnv) {
		this.m_resourceType = resourceType;
		this.m_runtimeEnv = runtimeEnv;
	}
	
	@Override
	public String getResourceType() {		return this.m_resourceType;	}

	@Override
	public HAPInfoResourceIdNormalize normalizeSimpleResourceId(HAPResourceIdSimple resourceId) {
		return new HAPInfoResourceIdNormalize(resourceId, null, resourceId.getResourceType());
	}
	
	@Override
	public HAPInfoResourceIdNormalize normalizeLocalResourceId(HAPResourceIdLocal resourceId) {
		return new HAPInfoResourceIdNormalize(resourceId, null, resourceId.getResourceType());
	}
	
	@Override
	public HAPIdEntityInDomain getResourceEntityBySimpleResourceId(HAPResourceIdSimple resourceId, HAPDomainEntityDefinitionGlobal globalDomain) {
		//normalize resource id by root resource
		HAPInfoResourceIdNormalize normalizedResourceId = this.normalizeSimpleResourceId(resourceId);
		HAPResourceIdSimple rootResourceId = (HAPResourceIdSimple)normalizedResourceId.getRootResourceId();
		
		//get root entity id
		HAPIdEntityInDomain rootEntityId = null;
		HAPResourceDefinition rootResourceDef = globalDomain.getResourceDefinitionByResourceId(rootResourceId);
		if(rootResourceDef==null) {
			//if root resource not loaded in domain, load it
			HAPDomainEntityDefinitionSimpleResource resourceDomain = globalDomain.newResourceDomain(rootResourceId);
			
			//get location information
			HAPInfoResourceLocation resourceLocInfo = HAPUtilityResourceId.getResourceLocationInfo(rootResourceId);
			resourceDomain.setLocationBase(resourceLocInfo.getBasePath());
			//read content
			JSONObject entityJsonObj = new JSONObject(HAPFileUtility.readFile(resourceLocInfo.getFiile()));
			//parse json object
			rootEntityId = parseEntity(entityJsonObj, new HAPContextParser(globalDomain, resourceDomain.getDomainId()));
			resourceDomain.setRootEntityId(rootEntityId);
		}
		else {
			//if root resource already loaded
			rootEntityId = rootResourceDef.getEntityId();
		}
		
		//get resource entity id by path
		HAPIdEntityInDomain out = HAPUtilityDomain.getEntityDescent(rootEntityId, normalizedResourceId.getPath(), globalDomain);
		return out;
	}

	@Override
	public HAPIdEntityInDomain getResourceEntityByLocalResourceId(HAPResourceIdLocal localResourceId, HAPDomainEntityDefinitionGlobal globalDomain, String currentDomainId) {
		//normalize resource id by root resource
		HAPInfoResourceIdNormalize normalizedResourceId = this.normalizeLocalResourceId(localResourceId);
		HAPResourceIdLocal rootResourceId = (HAPResourceIdLocal)normalizedResourceId.getRootResourceId();
		
		//get root entity id
		HAPDomainEntityDefinitionSimpleResource currentDomain = globalDomain.getResourceDomainById(currentDomainId);
		HAPIdEntityInDomain rootEntityId = null;
		HAPResourceDefinition rootResourceDef = currentDomain.getLocalResourceDefinition(rootResourceId);
		if(rootResourceDef==null) {
			//if root resource not loaded in domain, load it
			HAPPathLocationBase localRefBase = currentDomain.getLocationBase();
			String path = localRefBase.getPath() + rootResourceId.getResourceType() + "/" + rootResourceId.getName() + ".res";
			rootEntityId = this.parseEntity(HAPFileUtility.readFile(path), new HAPContextParser(globalDomain, currentDomainId));
		}
		else {
			//if root resource already loaded
			rootEntityId = rootResourceDef.getEntityId();
		}
		
		//get resource entity id by path
		HAPIdEntityInDomain out = HAPUtilityDomain.getEntityDescent(rootEntityId, normalizedResourceId.getPath(), globalDomain);
		return out;
	}

	private HAPIdEntityInDomain parseEntity(Object content, HAPContextParser parserContext) {
		JSONObject jsonObj = null;
		if(content instanceof JSONObject) jsonObj = (JSONObject)content;
		else if(content instanceof String)  jsonObj = new JSONObject(HAPJsonUtility.formatJson((String)content));
		HAPIdEntityInDomain entityId = HAPUtilityParserEntity.parseEntity(jsonObj, this.getResourceType(), parserContext, this.m_runtimeEnv.getDomainEntityManager(), this.m_runtimeEnv.getResourceDefinitionManager()); 
		return entityId;
	}

	protected HAPRuntimeEnvironment getRuntimeEnvironment() {    return this.m_runtimeEnv;    }
	
}
