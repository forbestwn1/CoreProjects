package com.nosliw.data.core.imp.runtime.js.resource;

import java.util.List;

import com.nosliw.data.core.imp.runtime.js.HAPDataAccessRuntimeJS;
import com.nosliw.data.core.runtime.HAPLoadResourceResponse;
import com.nosliw.data.core.runtime.HAPResource;
import com.nosliw.data.core.runtime.HAPResourceId;
import com.nosliw.data.core.runtime.HAPResourceManager;

public class HAPResourceManagerJSHelper implements HAPResourceManager{

	private HAPDataAccessRuntimeJS m_dataAccess = null;
	
	public HAPResourceManagerJSHelper(HAPDataAccessRuntimeJS dataAccess){
		this.m_dataAccess = dataAccess;
	}
	
	@Override
	public HAPLoadResourceResponse getResources(List<HAPResourceId> resourcesId) {
		HAPLoadResourceResponse out = new HAPLoadResourceResponse();
		for(HAPResourceId resourceId : resourcesId){
			HAPResource resource = this.getResource(resourceId);
			if(resource!=null)  out.addLoadedResource(resource);
			else  out.addFaildResourceId(resourceId);
		}
		return out;
	}

	@Override
	public HAPResource getResource(HAPResourceId resourceId) {
		HAPResourceDataHelperImp helperResource = this.m_dataAccess.getResourceHelper(resourceId.getId());
		if(helperResource!=null)		return new HAPResource(resourceId, helperResource, null);
		else return null;
	}

}
