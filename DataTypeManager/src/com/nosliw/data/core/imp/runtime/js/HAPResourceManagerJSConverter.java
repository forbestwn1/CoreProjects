package com.nosliw.data.core.imp.runtime.js;

import java.util.ArrayList;
import java.util.List;

import com.nosliw.data.core.imp.io.HAPDBAccess;
import com.nosliw.data.core.runtime.HAPResource;
import com.nosliw.data.core.runtime.HAPResourceId;
import com.nosliw.data.core.runtime.HAPResourceManager;
import com.nosliw.data.core.runtime.js.HAPResourceIdConverter;

public class HAPResourceManagerJSConverter  implements HAPResourceManager{

	private HAPDBAccess m_dbAccess = HAPDBAccess.getInstance();
	
	@Override
	public List<HAPResource> getResources(List<HAPResourceId> resourcesId) {
		List<HAPResource> out = new ArrayList<HAPResource>();
		
		for(HAPResourceId resourceId : resourcesId){
			HAPResourceIdConverter resourceIdOperation = new HAPResourceIdConverter(resourceId);
			
			HAPResourceDataConverterImp converterResource = this.m_dbAccess.getDataTypeConverter(resourceIdOperation.getOperationId());
			out.add(new HAPResource(resourceId, converterResource, null));
		}
		return out;
	}

}
