package com.nosliw.data.core.runtime;

import java.util.ArrayList;
import java.util.List;

public abstract class HAPResourceManagerImp implements HAPResourceManager{

	@Override
	public HAPLoadResourceResponse getResources(List<HAPResourceId> resourcesId) {
		HAPLoadResourceResponse out = new HAPLoadResourceResponse();
		for(HAPResourceId resourceId : resourcesId){
			HAPResource resource = this.getResource(resourceId);
			if(resource!=null) out.addLoadedResource(resource);
			else out.addFaildResourceId(resourceId);
		}
		return out;
	}

	@Override
	public HAPResourceInfo discoverResource(HAPResourceId resourceId) {
		HAPResourceInfo resourceInfo = new HAPResourceInfo(resourceId);
		//add dependency first
		List<HAPResourceDependent> dependencys = this.getResourceDependency(resourceId);
		for(HAPResourceDependent dependency : dependencys){
			resourceInfo.addDependency(dependency);
		}
		return resourceInfo;
	}

	protected List<HAPResourceDependent> getResourceDependency(HAPResourceId resourceId){
		return new ArrayList<HAPResourceDependent>();
	}
	
}
