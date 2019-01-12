package com.nosliw.uiresource.resource;

import java.util.LinkedHashMap;
import java.util.Map;

import com.nosliw.data.core.runtime.HAPResource;
import com.nosliw.data.core.runtime.HAPResourceId;
import com.nosliw.data.core.runtime.HAPResourceManagerImp;
import com.nosliw.data.core.runtime.HAPRuntimeInfo;
import com.nosliw.data.core.runtime.js.HAPRuntimeJSUtility;
import com.nosliw.uiresource.HAPUIResourceManager;
import com.nosliw.uiresource.module.HAPExecutableModule;
import com.nosliw.uiresource.page.execute.HAPExecutableUIUnitResource;

public class HAPResourceManagerModule  extends HAPResourceManagerImp{

	private HAPUIResourceManager m_uiResourceMan;
	
	public HAPResourceManagerModule(HAPUIResourceManager uiResourceMan){
		this.m_uiResourceMan = uiResourceMan;
	}

	@Override
	public HAPResource getResource(HAPResourceId resourceId, HAPRuntimeInfo runtimeInfo) {
//		HAPResourceIdUIModule uiModuleId = new HAPResourceIdUIModule(resourceId); 
//		HAPExecutableModule uiModule = this.m_uiResourceMan.getUIModule(uiModuleId.getId());
//		if(uiModule==null)  return null;
//		HAPResourceDataUIResource resourceData = new HAPResourceDataUIModule(uiModule);
//		Map<String, Object> info = new LinkedHashMap<String, Object>();
//		info.put(HAPRuntimeJSUtility.RESOURCE_LOADPATTERN, HAPRuntimeJSUtility.RESOURCE_LOADPATTERN_FILE);
//		return new HAPResource(resourceId, resourceData, null);
		return null;
	}

}
