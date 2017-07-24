package com.nosliw.data.core.runtime.js;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import com.nosliw.data.core.runtime.HAPLoadResourceResponse;
import com.nosliw.data.core.runtime.HAPResource;
import com.nosliw.data.core.runtime.HAPResourceId;
import com.nosliw.data.core.runtime.HAPResourceManager;

public class HAPResourceManagerJS implements HAPResourceManager{

	private Map<String, HAPResourceManager> m_resourceMans = new LinkedHashMap<String, HAPResourceManager>();

	public void registerResourceManager(String type, HAPResourceManager resourceMan){
		this.m_resourceMans.put(type, resourceMan);
	}
	
	@Override
	public HAPResource getResource(HAPResourceId resourceId) {
		HAPResourceManager resourceMan = this.getResourceManager(resourceId.getType());
		HAPResource resource = resourceMan.getResource(resourceId);
		return resource;
	}
	
	@Override
	public HAPLoadResourceResponse getResources(List<HAPResourceId> resourcesId) {
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
			resourceResponses.put(resourceType, resourceMan.getResources(sortedResourcesId.get(resourceType)));
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
	
	private HAPResourceManager getResourceManager(String resourceType){
		return this.m_resourceMans.get(resourceType);
	}

}
