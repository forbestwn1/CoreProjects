package com.nosliw.core.xxx.resource;

import com.nosliw.common.serialization.HAPSerializationFormat;
import com.nosliw.common.utils.HAPUtilityFile;
import com.nosliw.core.resource.HAPInfoResourceIdNormalize;
import com.nosliw.core.resource.HAPResourceIdLocal;
import com.nosliw.core.resource.HAPResourceIdSimple;
import com.nosliw.core.resource.HAPUtilityResourceId;
import com.nosliw.data.core.component.HAPPathLocationBase;
import com.nosliw.data.core.domain.HAPContextParser;
import com.nosliw.data.core.domain.HAPDomainEntityDefinitionGlobal;
import com.nosliw.data.core.domain.HAPDomainEntityDefinitionLocal;
import com.nosliw.data.core.domain.HAPIdEntityInDomain;
import com.nosliw.data.core.domain.definition.HAPUtilityEntityDefinition;
import com.nosliw.data.core.runtime.HAPRuntimeEnvironment;

public abstract class HAPPluginResourceDefinitionImp implements HAPPluginResourceDefinition{

	private String m_resourceType;
	
	private HAPRuntimeEnvironment m_runtimeEnv;
	
	public HAPPluginResourceDefinitionImp(String resourceType, HAPRuntimeEnvironment runtimeEnv) {
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
		//first, check if resource has been loaded to global domain
		HAPResourceDefinition rootResourceDef = globalDomain.getResourceDefinitionByResourceId(rootResourceId);
		if(rootResourceDef==null) {
			//if root resource not loaded in domain, load it
			HAPDomainEntityDefinitionLocal resourceDomain = globalDomain.newLocalDomain(rootResourceId);
			
			//get location information
			HAPInfoResourceLocation resourceLocInfo = this.getResourceLocationInfo(rootResourceId);
			resourceDomain.setLocationBase(resourceLocInfo.getBasePath());
			//read content and parse it
			rootEntityId = parseEntity(HAPUtilityFile.readFile(resourceLocInfo.getFiile()), resourceLocInfo.getFormat(), new HAPContextParser(globalDomain, resourceDomain.getDomainId()));
			resourceDomain.setRootEntityId(rootEntityId);
			resourceDomain.getEntityInfoDefinition(rootEntityId).setResourceId(rootResourceId);
		}
		else {
			//if root resource already loaded
			rootEntityId = rootResourceDef.getEntityId();
		}
		
		//get resource entity id by path
		HAPIdEntityInDomain out = HAPUtilityEntityDefinition.getEntityDescent(rootEntityId, normalizedResourceId.getPath(), globalDomain);
		return out;
	}

	@Override
	public HAPIdEntityInDomain getResourceEntityByLocalResourceId(HAPResourceIdLocal localResourceId, HAPDomainEntityDefinitionGlobal globalDomain, String currentDomainId) {
		//normalize resource id by root resource
		HAPInfoResourceIdNormalize normalizedResourceId = this.normalizeLocalResourceId(localResourceId);
		HAPResourceIdLocal rootResourceId = (HAPResourceIdLocal)normalizedResourceId.getRootResourceId();
		
		//get root entity id
		HAPDomainEntityDefinitionLocal currentDomain = globalDomain.getLocalDomainById(currentDomainId);
		HAPIdEntityInDomain rootEntityId = null;
		HAPResourceDefinition rootResourceDef = currentDomain.getLocalResourceDefinition(rootResourceId);
		if(rootResourceDef==null) {
			//if root resource not loaded in domain, load it
			HAPPathLocationBase localRefBase = currentDomain.getLocationBase();
			
			HAPInfoResourceLocation resourceLocationInfo = HAPUtilityResourceId.getResourceLocationInfo(localRefBase.getPath() + "/" + rootResourceId.getResourceType(), rootResourceId.getName());
			rootEntityId = this.parseEntity(HAPUtilityFile.readFile(resourceLocationInfo.getFiile()), resourceLocationInfo.getFormat(), new HAPContextParser(globalDomain, currentDomainId));
			
//			String path = localRefBase.getPath() + rootResourceId.getResourceType() + "/" + rootResourceId.getName() + ".res";
//			rootEntityId = this.parseEntity(HAPUtilityFile.readFile(path), new HAPContextParser(globalDomain, currentDomainId));
		}
		else {
			//if root resource already loaded
			rootEntityId = rootResourceDef.getEntityId();
		}
		
		//get resource entity id by path
		HAPIdEntityInDomain out = HAPUtilityEntityDefinition.getEntityDescent(rootEntityId, normalizedResourceId.getPath(), globalDomain);
		return out;
	}

	abstract protected HAPIdEntityInDomain parseEntity(Object content, HAPSerializationFormat format, HAPContextParser parserContext);

	protected HAPInfoResourceLocation getResourceLocationInfo(HAPResourceIdSimple resourceId) {
		return HAPUtilityResourceId.getResourceLocationInfo(resourceId);
	}
	
	protected HAPRuntimeEnvironment getRuntimeEnvironment() {    return this.m_runtimeEnv;    }
	
}
