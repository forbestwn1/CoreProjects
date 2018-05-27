package com.nosliw.uiresource.resource;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import com.nosliw.data.core.runtime.HAPResource;
import com.nosliw.data.core.runtime.HAPResourceDependent;
import com.nosliw.data.core.runtime.HAPResourceId;
import com.nosliw.data.core.runtime.HAPResourceManagerImp;
import com.nosliw.data.core.runtime.js.HAPRuntimeJSUtility;
import com.nosliw.uiresource.HAPUIResourceManager;
import com.nosliw.uiresource.page.HAPUIDefinitionUnitResource;

public class HAPResourceManagerUIResource extends HAPResourceManagerImp{

	private HAPUIResourceManager m_uiResourceMan;
	
	public HAPResourceManagerUIResource(HAPUIResourceManager uiResourceMan){
		this.m_uiResourceMan = uiResourceMan;
	}
	
	@Override
	public HAPResource getResource(HAPResourceId resourceId) {
		HAPResourceIdUIResource uiResourceId = new HAPResourceIdUIResource(resourceId); 
		HAPUIDefinitionUnitResource uiResource = this.m_uiResourceMan.getUIResource(uiResourceId.getId());
		if(uiResource==null)  return null;
		HAPResourceDataUIResource resourceData = new HAPResourceDataUIResource(uiResource);
		Map<String, Object> info = new LinkedHashMap<String, Object>();
		info.put(HAPRuntimeJSUtility.RESOURCE_LOADPATTERN, HAPRuntimeJSUtility.RESOURCE_LOADPATTERN_FILE);
		return new HAPResource(resourceId, resourceData, null);
	}

	@Override
	protected List<HAPResourceDependent> getResourceDependency(HAPResourceId resourceId){
		HAPResourceIdUIResource uiResourceId = new HAPResourceIdUIResource(resourceId); 
		HAPUIDefinitionUnitResource uiResource = this.m_uiResourceMan.getUIResource(uiResourceId.getId());
		return uiResource.getResourceDependency();
	}

}
