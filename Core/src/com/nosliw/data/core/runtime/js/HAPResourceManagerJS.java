package com.nosliw.data.core.runtime.js;

import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;

import com.nosliw.data.core.runtime.HAPResource;
import com.nosliw.data.core.runtime.HAPResourceId;
import com.nosliw.data.core.runtime.HAPResourceManager;

public class HAPResourceManagerJS implements HAPResourceManager{

	private Map<String, HAPResourceManager> m_resourceMans = new LinkedHashMap<String, HAPResourceManager>();

	public void registerResourceManager(String type, HAPResourceManager resourceMan){
		this.m_resourceMans.put(type, resourceMan);
	}
	
	@Override
	public Set<HAPResource> getResources(Set<HAPResourceId> resourcesId) {
		Set<HAPResource> out = new HashSet<HAPResource>();
		
		//sort out resource by type
		Map<String, Set<HAPResourceId>> sortedResourcesId = new LinkedHashMap<String, Set<HAPResourceId>>();
		for(HAPResourceId resourceId : resourcesId){
			String type = resourceId.getType();
			Set<HAPResourceId> typedResourcesId = sortedResourcesId.get(type);
			if(typedResourcesId==null){
				typedResourcesId = new HashSet<HAPResourceId>();
				sortedResourcesId.put(type, typedResourcesId);
			}
			typedResourcesId.add(resourceId);
		}

		for(String resourceType : sortedResourcesId.keySet()){
			HAPResourceManager resourceMan = this.getResourceManager(resourceType);
			Set<HAPResource> reosurces = resourceMan.getResources(sortedResourcesId.get(resourceType));
			out.addAll(reosurces);
		}
		
		return out;
	}
	
	private HAPResourceManager getResourceManager(String resourceType){
		return this.m_resourceMans.get(resourceType);
	}
}
