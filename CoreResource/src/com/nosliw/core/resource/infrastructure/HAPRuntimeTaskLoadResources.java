package com.nosliw.core.resource.infrastructure;

import java.util.List;

import com.nosliw.core.resource.HAPResourceInfo;
import com.nosliw.core.runtime.execute.HAPTaskRuntime;

public abstract class HAPRuntimeTaskLoadResources extends HAPTaskRuntime{

	final public static String TASK = "LoadResources"; 

	private List<HAPResourceInfo> m_resourcesInfo;
	
	public HAPRuntimeTaskLoadResources(List<HAPResourceInfo> resourcesInfo){
		this.m_resourcesInfo = resourcesInfo;
	}
	
	public List<HAPResourceInfo> getResourcesInfo(){		return this.m_resourcesInfo;	}
	
	@Override
	public String getTaskType(){  return TASK; }

}
