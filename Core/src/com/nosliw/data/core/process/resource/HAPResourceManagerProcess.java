package com.nosliw.data.core.process.resource;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import com.nosliw.data.core.process.HAPExecutableProcess;
import com.nosliw.data.core.process.HAPManagerProcess;
import com.nosliw.data.core.resource.HAPResource;
import com.nosliw.data.core.resource.HAPResourceDependent;
import com.nosliw.data.core.resource.HAPResourceId;
import com.nosliw.data.core.resource.HAPResourceManagerImp;
import com.nosliw.data.core.runtime.HAPRuntimeInfo;
import com.nosliw.data.core.runtime.js.HAPRuntimeJSUtility;

public class HAPResourceManagerProcess  extends HAPResourceManagerImp{

	private HAPManagerProcess m_processMan;
	
	public HAPResourceManagerProcess(HAPManagerProcess processMan){
		this.m_processMan = processMan;
	}
	
	@Override
	public HAPResource getResource(HAPResourceId resourceId, HAPRuntimeInfo runtimeInfo) {
		HAPResourceIdProcess processId = new HAPResourceIdProcess(resourceId); 
		HAPExecutableProcess process = this.m_processMan.getProcess(processId.getProcessId());
		if(process==null)  return null;
		Map<String, Object> info = new LinkedHashMap<String, Object>();
		info.put(HAPRuntimeJSUtility.RESOURCE_LOADPATTERN, HAPRuntimeJSUtility.RESOURCE_LOADPATTERN_FILE);
		return new HAPResource(resourceId, process.toResourceData(runtimeInfo), null);
	}

	@Override
	protected List<HAPResourceDependent> getResourceDependency(HAPResourceId resourceId, HAPRuntimeInfo runtimeInfo){
		HAPResourceIdProcess processId = new HAPResourceIdProcess(resourceId); 
		HAPExecutableProcess process = this.m_processMan.getProcess(processId.getProcessId());
		return process.getResourceDependency(runtimeInfo);
	}
}
