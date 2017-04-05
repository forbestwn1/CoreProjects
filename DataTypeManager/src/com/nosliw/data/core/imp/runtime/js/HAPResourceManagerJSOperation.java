package com.nosliw.data.core.imp.runtime.js;

import java.util.HashSet;
import java.util.Set;

import com.nosliw.data.core.imp.io.HAPDBAccess;
import com.nosliw.data.core.runtime.HAPResource;
import com.nosliw.data.core.runtime.HAPResourceId;
import com.nosliw.data.core.runtime.HAPResourceManager;
import com.nosliw.data.core.runtime.js.HAPResourceIdOperation;

public class HAPResourceManagerJSOperation implements HAPResourceManager{

	private HAPDBAccess m_dbAccess = HAPDBAccess.getInstance();
	
	@Override
	public Set<HAPResource> getResources(Set<HAPResourceId> resourcesId) {
		Set<HAPResource> out = new HashSet<HAPResource>();
		
		for(HAPResourceId resourceId : resourcesId){
			HAPResourceIdOperation resourceIdOperation = new HAPResourceIdOperation(resourceId);
			
			HAPResourceDataOperationImp helperResource = this.m_dbAccess.getJSOperation(resourceIdOperation.getOperationId());
			out.add(new HAPResource(resourceId, helperResource, null));
		}
		return out;
	}

}
