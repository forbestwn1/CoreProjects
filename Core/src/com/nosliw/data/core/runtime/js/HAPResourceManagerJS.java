package com.nosliw.data.core.runtime.js;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.nosliw.data.core.runtime.HAPLoadResourceResponse;
import com.nosliw.data.core.runtime.HAPResource;
import com.nosliw.data.core.runtime.HAPResourceDependent;
import com.nosliw.data.core.runtime.HAPResourceId;
import com.nosliw.data.core.runtime.HAPResourceInfo;
import com.nosliw.data.core.runtime.HAPResourceManager;
import com.nosliw.data.core.runtime.HAPResourceManagerRoot;
import com.nosliw.data.core.runtime.HAPRuntimeInfo;

public class HAPResourceManagerJS implements HAPResourceManagerRoot{

	private Map<String, HAPResourceManager> m_resourceMans = new LinkedHashMap<String, HAPResourceManager>();

	@Override
	public void registerResourceManager(String type, HAPResourceManager resourceMan){
		this.m_resourceMans.put(type, resourceMan);
	}
	
	@Override
	public HAPLoadResourceResponse getResources(List<HAPResourceId> resourcesId, HAPRuntimeInfo runtimeInfo) {
		//sort out resource by type, so that we can do bulk load resources for one type  
		Map<String, List<HAPResourceId>> sortedResourcesId = new LinkedHashMap<String, List<HAPResourceId>>();
		for(HAPResourceId resourceId : resourcesId){
			String type = resourceId.getType();
			List<HAPResourceId> typedResourcesId = sortedResourcesId.get(type);
			if(typedResourcesId==null){
				typedResourcesId = new ArrayList<HAPResourceId>();
				sortedResourcesId.put(type, typedResourcesId);
			}
			typedResourcesId.add(resourceId);
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
			HAPLoadResourceResponse resourceResponse = resourceResponses.get(resourceId.getType());
			HAPResource resource = resourceResponse.getLoadedResource(resourceId);
			if(resource!=null){
				out.addLoadedResource(resource);
			}
			else{
				out.addFaildResourceId(resourceId);
			}
		}
		return out;
	}
	
	@Override
	public List<HAPResourceInfo> discoverResources(List<HAPResourceId> resourceIds, HAPRuntimeInfo runtimeInfo) {
		List<HAPResourceInfo> out = new ArrayList<HAPResourceInfo>();
		Set<HAPResourceId> processedResources = new HashSet<HAPResourceId>();
	
		for(HAPResourceId resourceId : resourceIds){
			this.discoverResource(resourceId, out, processedResources, runtimeInfo);
		}
		
		return out;
	}

	private void discoverResource(HAPResourceId resourceId, List<HAPResourceInfo> resourceInfos, Set<HAPResourceId> processedResourceIds, HAPRuntimeInfo runtimeInfo){
		if(!processedResourceIds.contains(resourceId)){
			processedResourceIds.add(resourceId);
			HAPResourceInfo resourceInfo = this.getResourceManager(resourceId.getType()).discoverResource(resourceId, runtimeInfo);
			//add dependency first
			List<HAPResourceDependent> dependencys = resourceInfo.getDependency();
			for(HAPResourceDependent dependency : dependencys){
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
