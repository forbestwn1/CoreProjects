package com.nosliw.data.core.process.resource;

import java.util.List;

import com.nosliw.data.core.process.plugin.HAPManagerActivityPlugin;
import com.nosliw.data.core.process.plugin.HAPPluginActivity;
import com.nosliw.data.core.resource.HAPResource;
import com.nosliw.data.core.resource.HAPResourceDependency;
import com.nosliw.data.core.resource.HAPResourceId;
import com.nosliw.data.core.resource.HAPResourceManagerImp;
import com.nosliw.data.core.resource.HAPResourceUtility;
import com.nosliw.data.core.runtime.HAPRuntimeInfo;

public class HAPResourceManagerActivityPlugin  extends HAPResourceManagerImp{

	private HAPManagerActivityPlugin m_pluginMan;
	
	public HAPResourceManagerActivityPlugin(HAPManagerActivityPlugin pluginMan){
		this.m_pluginMan = pluginMan;
	}
	
	@Override
	public HAPResource getResource(HAPResourceId resourceId, HAPRuntimeInfo runtimeInfo) {
		HAPResourceIdActivityPlugin activityPluginId = new HAPResourceIdActivityPlugin(resourceId);
		HAPPluginActivity activityPlugin = this.m_pluginMan.getPlugin(activityPluginId.getActivityPlugId().getId());
		if(activityPlugin==null)  return null;
		
		HAPResourceDataActivityPlugin resourceData = new HAPResourceDataActivityPlugin(activityPlugin, runtimeInfo.getLanguage());
		return new HAPResource(resourceId, resourceData, HAPResourceUtility.buildResourceLoadPattern(resourceId, null));
	}

	@Override
	protected List<HAPResourceDependency> getResourceDependency(HAPResourceId resourceId, HAPRuntimeInfo runtimeInfo){
		return null;
	}
}
