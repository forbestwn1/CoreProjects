package com.nosliw.data.core.imp.runtime.js;

import java.util.List;

import com.nosliw.data.core.runtime.HAPLoadResourceResponse;
import com.nosliw.data.core.runtime.HAPResource;
import com.nosliw.data.core.runtime.HAPResourceId;
import com.nosliw.data.core.runtime.HAPResourceManager;
import com.nosliw.data.core.runtime.js.HAPResourceDataJSGateway;

public class HAPResourceManagerJSGateway implements HAPResourceManager{

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
		if(resourceId.getId().equals("discoveryGateway")){
			return new HAPResource(resourceId, new HAPResourceDataJSGateway(this, resourceId.getId()), null);
		}
		return null;
	}

	public void hello(String expression, Object parmsObj){
		System.out.println("HHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHHH");
	}
	

}
