package com.nosliw.uiresource.resource;

import java.util.LinkedHashMap;
import java.util.Map;

import com.nosliw.common.utils.HAPFileUtility;
import com.nosliw.data.core.runtime.HAPResource;
import com.nosliw.data.core.runtime.HAPResourceId;
import com.nosliw.data.core.runtime.HAPResourceManagerImp;
import com.nosliw.data.core.runtime.HAPRuntimeInfo;
import com.nosliw.data.core.runtime.js.HAPResourceDataFactory;
import com.nosliw.data.core.runtime.js.HAPRuntimeJSUtility;

public class HAPResourceManagerUIModuleEnv extends HAPResourceManagerImp{

	@Override
	public HAPResource getResource(HAPResourceId resourceId, HAPRuntimeInfo runtimeInfo) {
		HAPResourceIdUIModuleEnv id = new HAPResourceIdUIModuleEnv(resourceId);
		String file = HAPFileUtility.getUIModuleEnvFolder()+id.getId()+".js";
		Map<String, Object> info = new LinkedHashMap<String, Object>();
		info.put(HAPRuntimeJSUtility.RESOURCE_LOADPATTERN, HAPRuntimeJSUtility.RESOURCE_LOADPATTERN_FILE);
		return new HAPResource(resourceId, HAPResourceDataFactory.createJSValueResourceData(HAPFileUtility.readFile(file)), info);
	}

}
