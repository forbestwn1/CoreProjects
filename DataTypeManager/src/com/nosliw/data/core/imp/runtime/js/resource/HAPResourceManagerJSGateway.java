package com.nosliw.data.core.imp.runtime.js.resource;

import com.nosliw.data.core.resource.HAPResource;
import com.nosliw.data.core.resource.HAPResourceId;
import com.nosliw.data.core.resource.HAPResourceManagerImp;
import com.nosliw.data.core.resource.HAPResourceManagerRoot;
import com.nosliw.data.core.runtime.HAPRuntimeInfo;
import com.nosliw.data.core.runtime.js.resource.HAPResourceDataJSGateway;

public class HAPResourceManagerJSGateway extends HAPResourceManagerImp{

	public HAPResourceManagerJSGateway(HAPResourceManagerRoot rootResourceMan) {
		super(rootResourceMan);
	}

	@Override
	public HAPResource getResource(HAPResourceId resourceId, HAPRuntimeInfo runtimeInfo) {
		return new HAPResource(resourceId, new HAPResourceDataJSGateway(resourceId.getCoreIdLiterate()), null);
	}

}
