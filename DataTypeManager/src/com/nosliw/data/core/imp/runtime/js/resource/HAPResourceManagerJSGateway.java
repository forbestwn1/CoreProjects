package com.nosliw.data.core.imp.runtime.js.resource;

import com.nosliw.data.core.runtime.HAPResource;
import com.nosliw.data.core.runtime.HAPResourceId;
import com.nosliw.data.core.runtime.HAPResourceManagerImp;
import com.nosliw.data.core.runtime.HAPRuntimeInfo;
import com.nosliw.data.core.runtime.js.resource.HAPResourceDataJSGateway;

public class HAPResourceManagerJSGateway extends HAPResourceManagerImp{

	@Override
	public HAPResource getResource(HAPResourceId resourceId, HAPRuntimeInfo runtimeInfo) {
		return new HAPResource(resourceId, new HAPResourceDataJSGateway(resourceId.getId()), null);
	}

}
