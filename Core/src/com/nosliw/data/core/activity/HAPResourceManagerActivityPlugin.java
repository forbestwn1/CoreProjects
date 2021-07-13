package com.nosliw.data.core.activity;

import java.util.List;

import com.nosliw.data.core.resource.HAPResource;
import com.nosliw.data.core.resource.HAPResourceDependency;
import com.nosliw.data.core.resource.HAPResourceId;
import com.nosliw.data.core.resource.HAPResourceIdSimple;
import com.nosliw.data.core.resource.HAPResourceManagerImp;
import com.nosliw.data.core.resource.HAPResourceManagerRoot;
import com.nosliw.data.core.resource.HAPUtilityResource;
import com.nosliw.data.core.runtime.HAPRuntimeInfo;

public class HAPResourceManagerActivityPlugin  extends HAPResourceManagerImp{

	private HAPManagerActivityPlugin m_pluginMan;
	
	public HAPResourceManagerActivityPlugin(HAPManagerActivityPlugin pluginMan, HAPResourceManagerRoot rootResourceMan){
		super(rootResourceMan);
		this.m_pluginMan = pluginMan;
	}
	
	@Override
	public HAPResource getResource(HAPResourceId resourceId, HAPRuntimeInfo runtimeInfo) {
		HAPPluginResourceIdActivity activityPluginId = new HAPPluginResourceIdActivity((HAPResourceIdSimple)resourceId);
		HAPPluginActivity activityPlugin = this.m_pluginMan.getPlugin(activityPluginId.getActivityPlugId().getId());
		if(activityPlugin==null)  return null;
		
		HAPPluginResourceDataActivity resourceData = new HAPPluginResourceDataActivity(activityPlugin, runtimeInfo.getLanguage());
		return new HAPResource(resourceId, resourceData, HAPUtilityResource.buildResourceLoadPattern(resourceId, null));
	}

	@Override
	protected List<HAPResourceDependency> getResourceDependency(HAPResourceId resourceId, HAPRuntimeInfo runtimeInfo){
		return null;
	}
}
