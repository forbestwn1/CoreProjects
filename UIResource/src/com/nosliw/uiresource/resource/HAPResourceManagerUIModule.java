package com.nosliw.uiresource.resource;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import com.nosliw.data.core.resource.HAPResource;
import com.nosliw.data.core.resource.HAPResourceDependent;
import com.nosliw.data.core.resource.HAPResourceId;
import com.nosliw.data.core.resource.HAPResourceManagerImp;
import com.nosliw.data.core.runtime.HAPRuntimeInfo;
import com.nosliw.data.core.runtime.js.HAPRuntimeJSUtility;
import com.nosliw.uiresource.HAPUIResourceManager;
import com.nosliw.uiresource.module.HAPExecutableModule;

public class HAPResourceManagerUIModule  extends HAPResourceManagerImp{

	private HAPUIResourceManager m_uiResourceMan;
	
	public HAPResourceManagerUIModule(HAPUIResourceManager uiResourceMan){
		this.m_uiResourceMan = uiResourceMan;
	}

	@Override
	public HAPResource getResource(HAPResourceId resourceId, HAPRuntimeInfo runtimeInfo) {
		HAPResourceIdUIModule uiModuleId = new HAPResourceIdUIModule(resourceId); 
		HAPExecutableModule uiModule = this.m_uiResourceMan.getUIModule(uiModuleId.getId());
		if(uiModule==null)  return null;
		Map<String, Object> info = new LinkedHashMap<String, Object>();
		info.put(HAPRuntimeJSUtility.RESOURCE_LOADPATTERN, HAPRuntimeJSUtility.RESOURCE_LOADPATTERN_FILE);
		return new HAPResource(resourceId, uiModule.toResourceData(runtimeInfo), null);
	}

	@Override
	protected List<HAPResourceDependent> getResourceDependency(HAPResourceId resourceId, HAPRuntimeInfo runtimeInfo){
		HAPResourceIdUIModule uiModuleId = new HAPResourceIdUIModule(resourceId); 
		HAPExecutableModule uiModule = this.m_uiResourceMan.getUIModule(uiModuleId.getId());
		return uiModule.getResourceDependency(runtimeInfo);
	}
}
