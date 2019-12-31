package com.nosliw.uiresource.resource;

import java.util.List;

import com.nosliw.data.core.component.HAPAttachmentContainer;
import com.nosliw.data.core.resource.HAPResource;
import com.nosliw.data.core.resource.HAPResourceDependency;
import com.nosliw.data.core.resource.HAPResourceId;
import com.nosliw.data.core.resource.HAPResourceManagerImp;
import com.nosliw.data.core.resource.HAPResourceUtility;
import com.nosliw.data.core.runtime.HAPRuntimeInfo;
import com.nosliw.uiresource.HAPUIResourceManager;
import com.nosliw.uiresource.application.HAPExecutableAppEntry;

public class HAPResourceManagerUIAppEntry  extends HAPResourceManagerImp{

	private HAPUIResourceManager m_uiResourceMan;
	
	public HAPResourceManagerUIAppEntry(HAPUIResourceManager uiResourceMan){
		this.m_uiResourceMan = uiResourceMan;
	}

	@Override
	public HAPResource getResource(HAPResourceId resourceId, HAPRuntimeInfo runtimeInfo) {
		HAPResourceIdUIAppEntry uiAppId = new HAPResourceIdUIAppEntry(resourceId); 
		HAPExecutableAppEntry uiApp = this.m_uiResourceMan.getMiniApp(uiAppId.getUIAppEntryId().getAppId(), uiAppId.getUIAppEntryId().getEntry(), new HAPAttachmentContainer(resourceId.getSupplement()));
		if(uiApp==null)  return null;
		return new HAPResource(resourceId, uiApp.toResourceData(runtimeInfo), HAPResourceUtility.buildResourceLoadPattern(resourceId, null));
	}

	@Override
	protected List<HAPResourceDependency> getResourceDependency(HAPResourceId resourceId, HAPRuntimeInfo runtimeInfo){
		HAPResourceIdUIAppEntry uiAppId = new HAPResourceIdUIAppEntry(resourceId); 
		HAPExecutableAppEntry uiApp = this.m_uiResourceMan.getMiniApp(uiAppId.getUIAppEntryId().getAppId(), uiAppId.getUIAppEntryId().getEntry(), new HAPAttachmentContainer(resourceId.getSupplement()));
		return uiApp.getResourceDependency(runtimeInfo);
	}
}
