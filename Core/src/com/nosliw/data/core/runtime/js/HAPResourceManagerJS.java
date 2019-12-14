package com.nosliw.data.core.runtime.js;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.nosliw.common.utils.HAPSystemUtility;
import com.nosliw.data.core.resource.HAPLoadResourceResponse;
import com.nosliw.data.core.resource.HAPResource;
import com.nosliw.data.core.resource.HAPResourceDependency;
import com.nosliw.data.core.resource.HAPResourceId;
import com.nosliw.data.core.resource.HAPResourceInfo;
import com.nosliw.data.core.resource.HAPResourceManager;
import com.nosliw.data.core.resource.HAPResourceManagerRoot;
import com.nosliw.data.core.runtime.HAPRuntimeInfo;

public class HAPResourceManagerJS implements HAPResourceManagerRoot{

	private Map<String, HAPResourceManager> m_resourceMans = new LinkedHashMap<String, HAPResourceManager>();

	private Map<HAPResourceId, HAPResource> m_cachedResource = new LinkedHashMap<HAPResourceId, HAPResource>();
	private Map<HAPResourceId, List<HAPResourceInfo>> m_cachedDependency = new LinkedHashMap<HAPResourceId, List<HAPResourceInfo>>();
	

	@Override
	public void registerResourceManager(String type, HAPResourceManager resourceMan){
		this.m_resourceMans.put(type, resourceMan);
	}
	
	@Override
	public void clearCache() {
		m_cachedResource = new LinkedHashMap<HAPResourceId, HAPResource>();
		m_cachedDependency = new LinkedHashMap<HAPResourceId, List<HAPResourceInfo>>();
	}
	
	@Override
	public HAPLoadResourceResponse getResources(List<HAPResourceId> resourcesId, HAPRuntimeInfo runtimeInfo) {
		//sort out resource by type, so that we can do bulk load resources for one type  
		Map<String, List<HAPResourceId>> sortedResourcesId = new LinkedHashMap<String, List<HAPResourceId>>();
		Map<HAPResourceId, HAPResource> cachedResources = new LinkedHashMap<HAPResourceId, HAPResource>();
		for(HAPResourceId resourceId : resourcesId){
			HAPResource cachedResource = this.m_cachedResource.get(resourceId);
			if(cachedResource!=null) {
				cachedResources.put(resourceId, cachedResource);
			}
			else {
				String type = resourceId.getType();
				List<HAPResourceId> typedResourcesId = sortedResourcesId.get(type);
				if(typedResourcesId==null){
					typedResourcesId = new ArrayList<HAPResourceId>();
					sortedResourcesId.put(type, typedResourcesId);
				}
				typedResourcesId.add(resourceId);
			}
		}

		//load all resources according to type
		Map<String, HAPLoadResourceResponse> resourceResponses = new LinkedHashMap<String, HAPLoadResourceResponse>();
		for(String resourceType : sortedResourcesId.keySet()){
			HAPResourceManager resourceMan = this.getResourceManager(resourceType);
			resourceResponses.put(resourceType, resourceMan.getResources(sortedResourcesId.get(resourceType), runtimeInfo));
		}

		//merge all the response for different type, make sure the response follow the same squence as input
		HAPLoadResourceResponse out = new HAPLoadResourceResponse();
		for(HAPResourceId resourceId : resourcesId){
			HAPResource resource = cachedResources.get(resourceId);
			if(resource!=null) {
				out.addLoadedResource(resource);
			}
			else {
				HAPLoadResourceResponse resourceResponse = resourceResponses.get(resourceId.getType());
				resource = resourceResponse.getLoadedResource(resourceId);
				if(resource!=null){
					out.addLoadedResource(resource);
					if(HAPSystemUtility.getResourceCached()) this.m_cachedResource.put(resourceId, resource);
				}
				else{
					out.addFaildResourceId(resourceId);
				}
			}
		}
		return out;
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
				if(HAPSystemUtility.getResourceCached()) this.m_cachedDependency.put(resourceId, resourceDis);
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
			HAPResourceInfo resourceInfo = this.getResourceManager(resourceId.getType()).discoverResource(resourceId, runtimeInfo);
			//add dependency first
			List<HAPResourceDependency> dependencys = resourceInfo.getDependency();
			for(HAPResourceDependency dependency : dependencys){
				this.discoverResource(dependency.getId(), resourceInfos, processedResourceIds, runtimeInfo);
			}
			//then add itself
			resourceInfos.add(resourceInfo);
		}
	}
	
	private HAPResourceManager getResourceManager(String resourceType){
		return this.m_resourceMans.get(resourceType);
	}

}
