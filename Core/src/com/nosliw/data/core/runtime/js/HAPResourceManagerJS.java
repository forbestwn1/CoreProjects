package com.nosliw.data.core.runtime.js;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import com.nosliw.data.core.runtime.HAPResource;
import com.nosliw.data.core.runtime.HAPResourceId;
import com.nosliw.data.core.runtime.HAPResourceManager;

public class HAPResourceManagerJS implements HAPResourceManager{

	private Map<String, HAPResourceManager> m_resourceMans = new LinkedHashMap<String, HAPResourceManager>();

	public void registerResourceManager(String type, HAPResourceManager resourceMan){
		this.m_resourceMans.put(type, resourceMan);
	}
	
	@Override
	public List<HAPResource> getResources(List<HAPResourceId> resourcesId) {
		List<HAPResource> out = new ArrayList<HAPResource>();
		
		//sort out resource by type
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

		for(String resourceType : sortedResourcesId.keySet()){
			HAPResourceManager resourceMan = this.getResourceManager(resourceType);
			List<HAPResource> reosurces = resourceMan.getResources(sortedResourcesId.get(resourceType));
			out.addAll(reosurces);
		}
		
		return out;
	}
	
	private HAPResourceManager getResourceManager(String resourceType){
		return this.m_resourceMans.get(resourceType);
	}
}
