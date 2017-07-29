package com.nosliw.data.core.imp.runtime.js;

import java.util.List;

import com.nosliw.data.core.runtime.HAPLoadResourceResponse;
import com.nosliw.data.core.runtime.HAPResource;
import com.nosliw.data.core.runtime.HAPResourceId;
import com.nosliw.data.core.runtime.HAPResourceIdConverter;
import com.nosliw.data.core.runtime.HAPResourceManager;

public class HAPResourceManagerJSConverter  implements HAPResourceManager{

	private HAPDataAccessRuntimeJS m_dataAccess = null;
	
	public HAPResourceManagerJSConverter(HAPDataAccessRuntimeJS dataAccess){
		this.m_dataAccess = dataAccess;
	}
	
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
	public HAPResource getResource(HAPResourceId resourceId) {
		HAPResourceIdConverter resourceIdOperation = new HAPResourceIdConverter(resourceId);
		HAPResourceDataJSConverterImp converterResource = this.m_dataAccess.getDataTypeConverter(resourceIdOperation.getConverter());
		if(converterResource!=null)		return new HAPResource(resourceId, converterResource, null);
		else return null;
	}

}
