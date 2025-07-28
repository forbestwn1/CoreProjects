package com.nosliw.core.resource;

import java.util.List;

import com.nosliw.core.runtime.HAPRuntimeTask;
import com.nosliw.data.core.resource.HAPResourceInfo;

public abstract class HAPRuntimeTaskLoadResources extends HAPRuntimeTask{

	final public static String TASK = "LoadResources"; 

	private List<HAPResourceInfo> m_resourcesInfo;
	
	public HAPRuntimeTaskLoadResources(List<HAPResourceInfo> resourcesInfo){
		this.m_resourcesInfo = resourcesInfo;
	}
	
	public List<HAPResourceInfo> getResourcesInfo(){		return this.m_resourcesInfo;	}
	
	@Override
	public String getTaskType(){  return TASK; }

}
