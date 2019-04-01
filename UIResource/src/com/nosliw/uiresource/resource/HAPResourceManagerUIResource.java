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
import com.nosliw.uiresource.page.execute.HAPExecutableUIUnitPage;

public class HAPResourceManagerUIResource extends HAPResourceManagerImp{

	private HAPUIResourceManager m_uiResourceMan;
	
	public HAPResourceManagerUIResource(HAPUIResourceManager uiResourceMan){
		this.m_uiResourceMan = uiResourceMan;
	}
	
	@Override
	public HAPResource getResource(HAPResourceId resourceId, HAPRuntimeInfo runtimeInfo) {
		HAPResourceIdUIResource uiResourceId = new HAPResourceIdUIResource(resourceId); 
		HAPExecutableUIUnitPage uiResource = this.m_uiResourceMan.getUIPage(uiResourceId.getId());
		if(uiResource==null)  return null;
		Map<String, Object> info = new LinkedHashMap<String, Object>();
		info.put(HAPRuntimeJSUtility.RESOURCE_LOADPATTERN, HAPRuntimeJSUtility.RESOURCE_LOADPATTERN_FILE);
		return new HAPResource(resourceId, uiResource.toResourceData(runtimeInfo), null);
	}

	@Override
	protected List<HAPResourceDependent> getResourceDependency(HAPResourceId resourceId, HAPRuntimeInfo runtimeInfo){
		HAPResourceIdUIResource uiResourceId = new HAPResourceIdUIResource(resourceId); 
		HAPExecutableUIUnitPage uiResource = this.m_uiResourceMan.getUIPage(uiResourceId.getId());
		return uiResource.getResourceDependency(runtimeInfo);
	}

}
