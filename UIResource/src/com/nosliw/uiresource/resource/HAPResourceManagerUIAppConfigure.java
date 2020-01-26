package com.nosliw.uiresource.resource;

import com.nosliw.common.utils.HAPFileUtility;
import com.nosliw.data.core.resource.HAPResource;
import com.nosliw.data.core.resource.HAPResourceId;
import com.nosliw.data.core.resource.HAPResourceManagerImp;
import com.nosliw.data.core.runtime.HAPRuntimeInfo;
import com.nosliw.data.core.runtime.js.HAPResourceDataFactory;

public class HAPResourceManagerUIAppConfigure extends HAPResourceManagerImp{

	@Override
	public HAPResource getResource(HAPResourceId resourceId, HAPRuntimeInfo runtimeInfo) {
		HAPResourceIdUIAppConfigure id = new HAPResourceIdUIAppConfigure(resourceId);
		String file = HAPFileUtility.getUIAppConfigureFolder()+id.getIdLiterate()+".js";
		return new HAPResource(resourceId, HAPResourceDataFactory.createJSValueResourceData(HAPFileUtility.readFile(file)), null);
	}

}
