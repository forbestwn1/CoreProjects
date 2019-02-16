package com.nosliw.data.core.process.resource;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import com.nosliw.data.core.process.plugin.HAPManagerActivityPlugin;
import com.nosliw.data.core.process.plugin.HAPPluginActivity;
import com.nosliw.data.core.runtime.HAPResource;
import com.nosliw.data.core.runtime.HAPResourceDependent;
import com.nosliw.data.core.runtime.HAPResourceId;
import com.nosliw.data.core.runtime.HAPResourceManagerImp;
import com.nosliw.data.core.runtime.HAPRuntimeInfo;
import com.nosliw.data.core.runtime.js.HAPRuntimeJSUtility;

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
		Map<String, Object> info = new LinkedHashMap<String, Object>();
		info.put(HAPRuntimeJSUtility.RESOURCE_LOADPATTERN, HAPRuntimeJSUtility.RESOURCE_LOADPATTERN_FILE);
		return new HAPResource(resourceId, resourceData, info);
	}

	@Override
	protected List<HAPResourceDependent> getResourceDependency(HAPResourceId resourceId, HAPRuntimeInfo runtimeInfo){
		return null;
	}
}
