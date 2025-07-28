package com.nosliw.core.resource;

import java.util.ArrayList;
import java.util.List;

import com.nosliw.common.exception.HAPErrorUtility;
import com.nosliw.data.core.runtime.HAPRuntimeInfo;

public abstract class HAPResourceManagerImp implements HAPResourceManager1{
 
	protected HAPManagerResource m_rootResourceMan;
	
	public HAPResourceManagerImp(HAPManagerResource rootResourceMan) {
		this.m_rootResourceMan = rootResourceMan;
	}
	
	@Override
	public HAPLoadResourceResponse getResources(List<HAPResourceId> resourcesId, HAPRuntimeInfo runtimeInfo) {
		HAPLoadResourceResponse out = new HAPLoadResourceResponse();
		for(HAPResourceId resourceId : resourcesId){
			HAPResource resource = this.getResource(resourceId, runtimeInfo);
			if(resource!=null) {
				out.addLoadedResource(resource);

//				System.out.println();
//				System.out.println("*********************** Load Resource Start ************************");
//				System.out.println(resource.toString());
//				System.out.println("*********************** Load Resource End ************************");
//				System.out.println();

			}
			else {
				out.addFaildResourceId(resourceId);
				HAPErrorUtility.invalid("resource does not exist :" + resourceId.toString());
			}
		}
		return out;
	}

	@Override
	public HAPResourceInfo discoverResource(HAPResourceId resourceId, HAPRuntimeInfo runtimeInfo) {
		HAPResourceInfo resourceInfo = new HAPResourceInfo(resourceId);
		//add dependency first
		List<HAPResourceDependency> dependencys = this.getResourceDependency(resourceId, runtimeInfo);
		if(dependencys!=null) {
			for(HAPResourceDependency dependency : dependencys){
				resourceInfo.addDependency(dependency);
			}
		}
		return resourceInfo;
	}

	protected List<HAPResourceDependency> getResourceDependency(HAPResourceId resourceId, HAPRuntimeInfo runtimeInfo){
		return new ArrayList<HAPResourceDependency>();
	}
	
}
