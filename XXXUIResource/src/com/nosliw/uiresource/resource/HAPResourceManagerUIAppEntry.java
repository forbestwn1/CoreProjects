package com.nosliw.uiresource.resource;

import java.util.List;

import com.nosliw.core.resource.HAPManagerResource;
import com.nosliw.core.resource.HAPResource;
import com.nosliw.core.resource.HAPResourceDependency;
import com.nosliw.core.resource.HAPResourceId;
import com.nosliw.core.resource.HAPUtilityResource;
import com.nosliw.core.xxx.resource.HAPResourceManagerImp;
import com.nosliw.data.core.runtime.HAPRuntimeInfo;
import com.nosliw.uiresource.HAPUIResourceManager;
import com.nosliw.uiresource.application.HAPExecutableAppEntry;

public class HAPResourceManagerUIAppEntry  extends HAPResourceManagerImp{

	private HAPUIResourceManager m_uiResourceMan;
	
	public HAPResourceManagerUIAppEntry(HAPUIResourceManager uiResourceMan, HAPManagerResource rootResourceMan){
		super(rootResourceMan);
		this.m_uiResourceMan = uiResourceMan;
	}

	@Override
	public HAPResource getResource(HAPResourceId resourceId, HAPRuntimeInfo runtimeInfo) {
		HAPExecutableAppEntry uiApp = this.m_uiResourceMan.getMiniAppEntry(resourceId); 
		if(uiApp==null)  return null;
		return new HAPResource(resourceId, uiApp.toResourceData(runtimeInfo), HAPUtilityResource.buildResourceLoadPattern(resourceId, null));
	}

	@Override
	protected List<HAPResourceDependency> getResourceDependency(HAPResourceId resourceId, HAPRuntimeInfo runtimeInfo){
		HAPExecutableAppEntry uiApp = this.m_uiResourceMan.getMiniAppEntry(resourceId); 
		return uiApp.getResourceDependency(runtimeInfo, this.m_rootResourceMan);
	}
}
