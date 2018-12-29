package com.nosliw.data.core.process.resource;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import com.nosliw.data.core.process.HAPManagerProcess;
import com.nosliw.data.core.process.HAPPluginActivity;
import com.nosliw.data.core.runtime.HAPResource;
import com.nosliw.data.core.runtime.HAPResourceDependent;
import com.nosliw.data.core.runtime.HAPResourceId;
import com.nosliw.data.core.runtime.HAPResourceManagerImp;
import com.nosliw.data.core.runtime.js.HAPRuntimeJSUtility;

public class HAPResourceManagerActivityPlugin  extends HAPResourceManagerImp{

	private HAPManagerProcess m_processMan;
	
	public HAPResourceManagerActivityPlugin(HAPManagerProcess processMan){
		this.m_processMan = processMan;
	}
	
	@Override
	public HAPResource getResource(HAPResourceId resourceId) {
		HAPResourceIdActivityPlugin activityPluginId = new HAPResourceIdActivityPlugin(resourceId); 
		HAPPluginActivity activityPlugin = this.m_processMan.getActivityPlugin(activityPluginId.getId());
		if(activityPlugin==null)  return null;
		HAPResourceDataActivityPlugin resourceData = new HAPResourceDataActivityPlugin(activityPlugin.getScript());
		Map<String, Object> info = new LinkedHashMap<String, Object>();
		info.put(HAPRuntimeJSUtility.RESOURCE_LOADPATTERN, HAPRuntimeJSUtility.RESOURCE_LOADPATTERN_FILE);
		return new HAPResource(resourceId, resourceData, null);
	}

	@Override
	protected List<HAPResourceDependent> getResourceDependency(HAPResourceId resourceId){
		return null;
	}
}
