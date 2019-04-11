package com.nosliw.uiresource.resource;

import java.util.LinkedHashMap;
import java.util.Map;

import com.nosliw.common.utils.HAPFileUtility;
import com.nosliw.data.core.resource.HAPResource;
import com.nosliw.data.core.resource.HAPResourceId;
import com.nosliw.data.core.resource.HAPResourceManagerImp;
import com.nosliw.data.core.runtime.HAPRuntimeInfo;
import com.nosliw.data.core.runtime.js.HAPResourceDataFactory;
import com.nosliw.data.core.runtime.js.HAPRuntimeJSUtility;

public class HAPResourceManagerUIModuleDecoration extends HAPResourceManagerImp{

	@Override
	public HAPResource getResource(HAPResourceId resourceId, HAPRuntimeInfo runtimeInfo) {
		HAPResourceIdUIModuleDecoration id = new HAPResourceIdUIModuleDecoration(resourceId);
		String file = HAPFileUtility.getUIModuleDecorationFolder()+id.getId()+".js";
		Map<String, Object> info = new LinkedHashMap<String, Object>();
		info.put(HAPRuntimeJSUtility.RESOURCE_LOADPATTERN, HAPRuntimeJSUtility.RESOURCE_LOADPATTERN_FILE);
		return new HAPResource(resourceId, HAPResourceDataFactory.createJSValueResourceData(HAPFileUtility.readFile(file)), info);
	}

}
