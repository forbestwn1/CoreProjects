package com.nosliw.uiresource.resource;

import java.util.ArrayList;
import java.util.List;

import com.nosliw.data.core.runtime.HAPResource;
import com.nosliw.data.core.runtime.HAPResourceDependent;
import com.nosliw.data.core.runtime.HAPResourceId;
import com.nosliw.data.core.runtime.HAPResourceManagerImp;
import com.nosliw.uiresource.tag.HAPUITagDefinition;
import com.nosliw.uiresource.tag.HAPUITagManager;

public class HAPResourceManagerUITag extends HAPResourceManagerImp{

	private HAPUITagManager m_uiTagMan;
	
	public HAPResourceManagerUITag(HAPUITagManager uiTagMan){
		this.m_uiTagMan = uiTagMan;
	}
	
	@Override
	public HAPResource getResource(HAPResourceId resourceId) {
		HAPResourceIdUITag uiTagResourceId = new HAPResourceIdUITag(resourceId); 
		HAPUITagDefinition uiTagDefinition = this.m_uiTagMan.getUITagDefinition(uiTagResourceId.getUITagId());
		if(uiTagDefinition==null)  return null;
		HAPResourceDataUITag resourceData = new HAPResourceDataUITag(uiTagDefinition);
		return new HAPResource(resourceId, resourceData, null);
	}

	@Override
	protected List<HAPResourceDependent> getResourceDependency(HAPResourceId resourceId){
		List<HAPResourceDependent> out = new ArrayList<HAPResourceDependent>();
		HAPResourceIdUITag uiTagResourceId = new HAPResourceIdUITag(resourceId); 
		HAPUITagDefinition uiTagDefinition = this.m_uiTagMan.getUITagDefinition(uiTagResourceId.getUITagId());
		out.addAll(uiTagDefinition.getResourceDependency());
		return out;
	}
	
}
