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
import com.nosliw.uiresource.module.HAPExecutableModule;

public class HAPResourceManagerUIModule  extends HAPResourceManagerImp{

	private HAPUIResourceManager m_uiResourceMan;
	
	public HAPResourceManagerUIModule(HAPUIResourceManager uiResourceMan){
		this.m_uiResourceMan = uiResourceMan;
	}

	@Override
	public HAPResource getResource(HAPResourceId resourceId, HAPRuntimeInfo runtimeInfo) {
		HAPResourceIdUIModule uiModuleId = new HAPResourceIdUIModule(resourceId); 
		HAPExecutableModule uiModule = this.m_uiResourceMan.getUIModule(uiModuleId.getId(), new HAPAttachmentContainer(resourceId.getSupplement()));
		if(uiModule==null)  return null;
		return new HAPResource(resourceId, uiModule.toResourceData(runtimeInfo), HAPResourceUtility.buildResourceLoadPattern(resourceId, null));
	}

	@Override
	protected List<HAPResourceDependency> getResourceDependency(HAPResourceId resourceId, HAPRuntimeInfo runtimeInfo){
		HAPResourceIdUIModule uiModuleId = new HAPResourceIdUIModule(resourceId); 
		HAPExecutableModule uiModule = this.m_uiResourceMan.getUIModule(uiModuleId.getId(), new HAPAttachmentContainer(resourceId.getSupplement()));
		return uiModule.getResourceDependency(runtimeInfo);
	}
}
