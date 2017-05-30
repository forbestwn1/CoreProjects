package com.nosliw.data.core.imp.runtime.js;

import java.util.ArrayList;
import java.util.List;

import com.nosliw.data.core.imp.io.HAPDBAccess;
import com.nosliw.data.core.runtime.HAPResource;
import com.nosliw.data.core.runtime.HAPResourceId;
import com.nosliw.data.core.runtime.HAPResourceIdOperation;
import com.nosliw.data.core.runtime.HAPResourceManager;

public class HAPResourceManagerJSOperation implements HAPResourceManager{

	private HAPDBAccess m_dbAccess = HAPDBAccess.getInstance();
	
	@Override
	public List<HAPResource> getResources(List<HAPResourceId> resourcesId) {
		List<HAPResource> out = new ArrayList<HAPResource>();
		
		for(HAPResourceId resourceId : resourcesId){
			HAPResourceIdOperation resourceIdOperation = new HAPResourceIdOperation(resourceId);
			
			HAPResourceDataJSOperationImp helperResource = this.m_dbAccess.getJSOperation(resourceIdOperation.getOperationId());
			out.add(new HAPResource(resourceId, helperResource, null));
		}
		return out;
	}

}
