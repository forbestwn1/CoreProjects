package com.nosliw.uiresource.resource;

import com.nosliw.common.utils.HAPFileUtility;
import com.nosliw.data.core.runtime.HAPResource;
import com.nosliw.data.core.runtime.HAPResourceId;
import com.nosliw.data.core.runtime.HAPResourceManagerImp;
import com.nosliw.data.core.runtime.HAPRuntimeInfo;
import com.nosliw.data.core.runtime.js.HAPResourceDataFactory;

public class HAPResourceManagerUIModuleEnv extends HAPResourceManagerImp{

	@Override
	public HAPResource getResource(HAPResourceId resourceId, HAPRuntimeInfo runtimeInfo) {
		HAPResourceIdUIModuleEnv id = new HAPResourceIdUIModuleEnv(resourceId);
		String file = HAPFileUtility.getUIModuleEnvFolder()+id.getId()+".js";
		return new HAPResource(resourceId, HAPResourceDataFactory.createJSValueResourceData(HAPFileUtility.readFile(file)), null);
	}

}
