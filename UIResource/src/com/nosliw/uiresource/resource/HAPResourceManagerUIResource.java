package com.nosliw.uiresource.resource;

import java.util.List;

import com.nosliw.data.core.resource.HAPResource;
import com.nosliw.data.core.resource.HAPResourceDependency;
import com.nosliw.data.core.resource.HAPResourceId;
import com.nosliw.data.core.resource.HAPResourceManagerImp;
import com.nosliw.data.core.resource.HAPResourceUtility;
import com.nosliw.data.core.runtime.HAPRuntimeInfo;
import com.nosliw.uiresource.HAPUIResourceManager;
import com.nosliw.uiresource.page.execute.HAPExecutableUIUnitPage;

public class HAPResourceManagerUIResource extends HAPResourceManagerImp{

	private HAPUIResourceManager m_uiResourceMan;
	
	public HAPResourceManagerUIResource(HAPUIResourceManager uiResourceMan){
		this.m_uiResourceMan = uiResourceMan;
	}
	
	@Override
	public HAPResource getResource(HAPResourceId resourceId, HAPRuntimeInfo runtimeInfo) {
		HAPExecutableUIUnitPage uiResource = this.m_uiResourceMan.getUIPage(resourceId);
		if(uiResource==null)  return null;
		return new HAPResource(resourceId, uiResource.toResourceData(runtimeInfo), HAPResourceUtility.buildResourceLoadPattern(resourceId, null));
	}

	@Override
	protected List<HAPResourceDependency> getResourceDependency(HAPResourceId resourceId, HAPRuntimeInfo runtimeInfo){
		HAPExecutableUIUnitPage uiResource = this.m_uiResourceMan.getUIPage(resourceId);
		return uiResource.getResourceDependency(runtimeInfo);
	}
}
