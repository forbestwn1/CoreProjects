package com.nosliw.data.core.process1.resource;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import com.nosliw.data.core.process1.HAPExecutableProcess;
import com.nosliw.data.core.process1.HAPManagerProcess;
import com.nosliw.data.core.resource.HAPResource;
import com.nosliw.data.core.resource.HAPResourceDependency;
import com.nosliw.data.core.resource.HAPResourceId;
import com.nosliw.data.core.resource.HAPResourceManagerImp;
import com.nosliw.data.core.resource.HAPResourceManagerRoot;
import com.nosliw.data.core.runtime.HAPRuntimeInfo;
import com.nosliw.data.core.runtime.js.HAPUtilityRuntimeJS;

public class HAPResourceManagerProcess  extends HAPResourceManagerImp{

	private HAPManagerProcess m_processMan;
	
	public HAPResourceManagerProcess(HAPManagerProcess processMan, HAPResourceManagerRoot rootResourceMan){
		super(rootResourceMan);
		this.m_processMan = processMan;
	}
	
	@Override
	public HAPResource getResource(HAPResourceId resourceId, HAPRuntimeInfo runtimeInfo) {
		HAPExecutableProcess process = this.m_processMan.getProcess(resourceId, null);
		if(process==null)  return null;
		Map<String, Object> info = new LinkedHashMap<String, Object>();
		info.put(HAPUtilityRuntimeJS.RESOURCE_LOADPATTERN, HAPUtilityRuntimeJS.RESOURCE_LOADPATTERN_FILE);
		return new HAPResource(resourceId, process.toResourceData(runtimeInfo), null);
	}

	@Override
	protected List<HAPResourceDependency> getResourceDependency(HAPResourceId resourceId, HAPRuntimeInfo runtimeInfo){
		HAPExecutableProcess process = this.m_processMan.getProcess(resourceId, null);
		return process.getResourceDependency(runtimeInfo, this.m_rootResourceMan);
	}
}
