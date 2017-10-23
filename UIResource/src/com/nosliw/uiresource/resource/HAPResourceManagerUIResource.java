package com.nosliw.uiresource.resource;

import java.util.List;

import com.nosliw.data.core.runtime.HAPLoadResourceResponse;
import com.nosliw.data.core.runtime.HAPResource;
import com.nosliw.data.core.runtime.HAPResourceDependent;
import com.nosliw.data.core.runtime.HAPResourceId;
import com.nosliw.data.core.runtime.HAPResourceInfo;
import com.nosliw.data.core.runtime.HAPResourceManager;
import com.nosliw.uiresource.HAPUIResource;
import com.nosliw.uiresource.HAPUIResourceManager;
import com.nosliw.uiresource.definition.HAPUIDefinitionUnitResource;

public class HAPResourceManagerUIResource implements HAPResourceManager{

	private HAPUIResourceManager m_uiResourceMan;
	
	public HAPResourceManagerUIResource(HAPUIResourceManager uiResourceMan){
		this.m_uiResourceMan = uiResourceMan;
	}
	
	@Override
	public HAPLoadResourceResponse getResources(List<HAPResourceId> resourcesId) {
		HAPLoadResourceResponse out = new HAPLoadResourceResponse();
		for(HAPResourceId resourceId : resourcesId){
			HAPResource resource = this.getResource(resourceId);
			if(resource!=null)  out.addLoadedResource(resource);
			else out.addFaildResourceId(resourceId);
		}
		return out;
	}

	@Override
	public HAPResource getResource(HAPResourceId resourceId) {
		HAPResourceIdUIResource uiResourceId = new HAPResourceIdUIResource(resourceId); 
		HAPUIDefinitionUnitResource uiResource = this.m_uiResourceMan.getUIResource(uiResourceId.getId());
		if(uiResource==null)  return null;
		HAPResourceDataUIResource resourceData = new HAPResourceDataUIResource(uiResource);
		return new HAPResource(resourceId, resourceData, null);
	}

	@Override
	public HAPResourceInfo discoverResource(HAPResourceId resourceId) {
		HAPResourceInfo out = new HAPResourceInfo(resourceId);
		HAPResourceIdUIResource uiResourceId = new HAPResourceIdUIResource(resourceId); 
		HAPUIDefinitionUnitResource uiResource = this.m_uiResourceMan.getUIResource(uiResourceId.getId());
		List<HAPResourceDependent> dependency = uiResource.getResourceDependency();
		for(HAPResourceDependent d : dependency)   out.addDependency(d);
		return out;
	}

}
