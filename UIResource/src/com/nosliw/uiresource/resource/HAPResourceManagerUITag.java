package com.nosliw.uiresource.resource;

import java.util.ArrayList;
import java.util.List;

import com.nosliw.data.core.resource.HAPResource;
import com.nosliw.data.core.resource.HAPResourceDependency;
import com.nosliw.data.core.resource.HAPResourceId;
import com.nosliw.data.core.resource.HAPResourceIdSimple;
import com.nosliw.data.core.resource.HAPResourceManagerImp;
import com.nosliw.data.core.resource.HAPResourceManager;
import com.nosliw.data.core.resource.HAPUtilityResource;
import com.nosliw.data.core.runtime.HAPRuntimeInfo;
import com.nosliw.ui.tag.HAPManagerUITag;
import com.nosliw.uiresource.page.tag.HAPUITagDefinition;

public class HAPResourceManagerUITag extends HAPResourceManagerImp{

	private HAPManagerUITag m_uiTagMan;

	public HAPResourceManagerUITag(HAPManagerUITag uiTagMan, HAPResourceManager rootResourceMan){
		super(rootResourceMan);
		this.m_uiTagMan = uiTagMan;
	}
	
	@Override
	public HAPResource getResource(HAPResourceId resourceId, HAPRuntimeInfo runtimeInfo) {
		HAPResourceIdUITag uiTagResourceId = new HAPResourceIdUITag((HAPResourceIdSimple)resourceId); 
		HAPUITagDefinition uiTagDefinition = this.m_uiTagMan.getUITagDefinition(uiTagResourceId.getUITagId());
		if(uiTagDefinition==null)  return null;
		HAPResourceDataUITag resourceData = new HAPResourceDataUITag(uiTagDefinition);
		
		return new HAPResource(resourceId, resourceData, HAPUtilityResource.buildResourceLoadPattern(resourceId, null));
	}

	@Override
	protected List<HAPResourceDependency> getResourceDependency(HAPResourceId resourceId, HAPRuntimeInfo runtimeInfo){
		List<HAPResourceDependency> out = new ArrayList<HAPResourceDependency>();
		HAPResourceIdUITag uiTagResourceId = new HAPResourceIdUITag((HAPResourceIdSimple)resourceId); 
		HAPUITagDefinition uiTagDefinition = this.m_uiTagMan.getUITagDefinition(uiTagResourceId.getUITagId());
		out.addAll(uiTagDefinition.getResourceDependency());
		return out;
	}
	
}
