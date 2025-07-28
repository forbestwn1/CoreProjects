package com.nosliw.core.resource.imp.js;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.nosliw.common.path.HAPPath;
import com.nosliw.common.utils.HAPConstantShared;
import com.nosliw.data.core.resource.HAPIdResourceType;
import com.nosliw.data.core.resource.HAPLoadResourceResponse;
import com.nosliw.data.core.resource.HAPPluginResourceManager;
import com.nosliw.data.core.resource.HAPResource;
import com.nosliw.data.core.resource.HAPResourceData;
import com.nosliw.data.core.resource.HAPResourceDataOrWrapper;
import com.nosliw.data.core.resource.HAPResourceDependency;
import com.nosliw.data.core.resource.HAPResourceId;
import com.nosliw.data.core.resource.HAPResourceIdEmbeded;
import com.nosliw.data.core.resource.HAPResourceIdSimple;
import com.nosliw.data.core.resource.HAPResourceInfo;
import com.nosliw.data.core.resource.HAPManagerResource;
import com.nosliw.data.core.resource.HAPUtilityResource;
import com.nosliw.data.core.resource.HAPWrapperResourceData;
import com.nosliw.data.core.runtime.HAPRuntimeInfo;
import com.nosliw.data.core.system.HAPSystemUtility;

public class HAPManagerResourceJS implements HAPManagerResource{

	private Map<String, HAPPluginResourceManager> m_resourceManPlugins = new LinkedHashMap<String, HAPPluginResourceManager>();

	private Map<HAPResourceId, HAPResource> m_cachedResource = new LinkedHashMap<HAPResourceId, HAPResource>();
	private Map<HAPResourceId, List<HAPResourceInfo>> m_cachedDependency = new LinkedHashMap<HAPResourceId, List<HAPResourceInfo>>();
	

	@Override
	public void registerResourceManagerPlugin(HAPIdResourceType resourceType, HAPPluginResourceManager resourceManPlugin){
		this.m_resourceManPlugins.put(resourceType.getKey(), resourceManPlugin);
	}
	
	@Override
	public void clearCache() {
		m_cachedResource = new LinkedHashMap<HAPResourceId, HAPResource>();
		m_cachedDependency = new LinkedHashMap<HAPResourceId, List<HAPResourceInfo>>();
	}
	
	@Override
	public HAPLoadResourceResponse getResources(List<HAPResourceId> resourcesId, HAPRuntimeInfo runtimeInfo) {
		HAPLoadResourceResponse out = new HAPLoadResourceResponse();
		for(HAPResourceId resourceId : resourcesId){
			HAPResource resource = getResource(resourceId, runtimeInfo);
			if(resource==null) {
				out.addFaildResourceId(resourceId);
			} else {
				out.addLoadedResource(resource);
			}
		}
		return out;
	}

	private HAPResource getResource(HAPResourceId resourceId, HAPRuntimeInfo runtimeInfo) {
		return new HAPResource(resourceId, getResourceData(resourceId, runtimeInfo), HAPUtilityResource.buildResourceLoadPattern(resourceId, null));
	}
	
	private HAPResourceData getResourceData(HAPResourceId resourceId, HAPRuntimeInfo runtimeInfo) {
		HAPResourceData resourceData = null;
		String structureType = resourceId.getStructure();
		if(structureType.equals(HAPConstantShared.RESOURCEID_TYPE_SIMPLE)) {
			HAPResourceIdSimple simpleResourceId = (HAPResourceIdSimple)resourceId;
			HAPPluginResourceManager resourceManPlugin = this.getResourceManagerPlugin(simpleResourceId.getResourceTypeId());
			HAPResourceDataOrWrapper dataOrWrapper = resourceManPlugin.getResourceData(simpleResourceId, runtimeInfo);
			if(dataOrWrapper==null) {
				throw new RuntimeException();
			}
			if(dataOrWrapper instanceof HAPResourceData) {
				resourceData = (HAPResourceData)dataOrWrapper;
			} else if(dataOrWrapper instanceof HAPWrapperResourceData) {
				resourceData = ((HAPWrapperResourceData)dataOrWrapper).getResourceData();
			}
		}
		else if(structureType.equals(HAPConstantShared.RESOURCEID_TYPE_EMBEDED)) {
			HAPResourceIdEmbeded embededResourceId = (HAPResourceIdEmbeded)resourceId;
			HAPResourceIdSimple parentResourceId = embededResourceId.getParentResourceId();
			HAPPluginResourceManager resourceManPlugin = this.getResourceManagerPlugin(parentResourceId.getResourceTypeId());
			HAPResourceDataOrWrapper dataOrWrapper = resourceManPlugin.getResourceData(parentResourceId, runtimeInfo);
			resourceData = (HAPResourceData)dataOrWrapper.getDescendant(new HAPPath(embededResourceId.getPath()));
		}
		return resourceData;
	}
	
	@Override
	public List<HAPResourceInfo> discoverResources(List<HAPResourceId> resourceIds, HAPRuntimeInfo runtimeInfo) {
		List<HAPResourceInfo> out = new ArrayList<HAPResourceInfo>();
	
		for(HAPResourceId resourceId : resourceIds){
			List<HAPResourceInfo> cached = this.m_cachedDependency.get(resourceId);
			if(cached==null) {
				List<HAPResourceInfo> resourceDis = new ArrayList<HAPResourceInfo>();
				Set<HAPResourceId> processedResources = new HashSet<HAPResourceId>();
				this.discoverResource(resourceId, resourceDis, processedResources, runtimeInfo);
				out.addAll(resourceDis);
				if(HAPSystemUtility.getResourceCached()) {
					this.m_cachedDependency.put(resourceId, resourceDis);
				}
			}
			else {
				out.addAll(cached);
			}
		}
		
		return out;
	}

	private void discoverResource(HAPResourceId resourceId, List<HAPResourceInfo> resourceInfos, Set<HAPResourceId> processedResourceIds, HAPRuntimeInfo runtimeInfo){
		if(!processedResourceIds.contains(resourceId)){
			processedResourceIds.add(resourceId);
			
			HAPResourceInfo resourceInfo = new HAPResourceInfo(resourceId); 
			//add dependency first
			List<HAPResourceDependency> dependencys = getResourceData(resourceId, runtimeInfo).getResourceDependency(runtimeInfo);
			for(HAPResourceDependency dependency : dependencys){
				resourceInfo.addDependency(dependency);
				this.discoverResource(dependency.getId(), resourceInfos, processedResourceIds, runtimeInfo);
			}
			//then add itself
			resourceInfos.add(resourceInfo);
		}
	}
	
	private HAPPluginResourceManager getResourceManagerPlugin(HAPIdResourceType resourceType){
		return this.m_resourceManPlugins.get(resourceType.getKey());
	}

//	@Override
	public HAPLoadResourceResponse getResources1(List<HAPResourceId> resourcesId, HAPRuntimeInfo runtimeInfo) {
		//sort out resource by type, so that we can do bulk load resources for one type  
		Map<HAPIdResourceType, List<HAPResourceId>> sortedResourcesId = new LinkedHashMap<HAPIdResourceType, List<HAPResourceId>>();
		Map<HAPResourceId, HAPResource> cachedResources = new LinkedHashMap<HAPResourceId, HAPResource>();
		for(HAPResourceId resourceId : resourcesId){
			HAPResource cachedResource = this.m_cachedResource.get(resourceId);
			if(cachedResource!=null) {
				cachedResources.put(resourceId, cachedResource);
			}
			else {
				HAPIdResourceType resourceTypeId = resourceId.getResourceTypeId();
				List<HAPResourceId> typedResourcesId = sortedResourcesId.get(resourceTypeId);
				if(typedResourcesId==null){
					typedResourcesId = new ArrayList<HAPResourceId>();
					sortedResourcesId.put(resourceTypeId, typedResourcesId);
				}
				typedResourcesId.add(resourceId);
			}
		}

		//load all resources according to type
		Map<String, HAPLoadResourceResponse> resourceResponses = new LinkedHashMap<String, HAPLoadResourceResponse>();
		for(HAPIdResourceType resourceType : sortedResourcesId.keySet()){
			HAPPluginResourceManager resourceManPlugin = this.getResourceManagerPlugin(resourceType);
			resourceResponses.put(resourceType, resourceManPlugin.getResources(sortedResourcesId.get(resourceType), runtimeInfo));
		}

		//merge all the response for different type, make sure the response follow the same squence as input
		HAPLoadResourceResponse out = new HAPLoadResourceResponse();
		for(HAPResourceId resourceId : resourcesId){
			HAPResource resource = cachedResources.get(resourceId);
			if(resource!=null) {
				out.addLoadedResource(resource);
			}
			else {
				HAPLoadResourceResponse resourceResponse = resourceResponses.get(resourceId.getResourceTypeId());
				resource = resourceResponse.getLoadedResource(resourceId);
				if(resource!=null){
					out.addLoadedResource(resource);
					if(HAPSystemUtility.getResourceCached()) {
						this.m_cachedResource.put(resourceId, resource);
					}
				}
				else{
					out.addFaildResourceId(resourceId);
				}
			}
		}
		return out;
	}
	

}
