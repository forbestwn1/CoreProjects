package com.nosliw.data.core.runtime;

import java.util.List;

public abstract class HAPLoadResourcesTask extends HAPRuntimeTask{

	private List<HAPResourceId> m_resourcesId;
	
	public HAPLoadResourcesTask(List<HAPResourceId> resourcesId){
		this.m_resourcesId = resourcesId;
	}
	
	public List<HAPResourceId> getResourcesId(){		return this.m_resourcesId;	}
	
	@Override
	public String getTaskType(){  return "LoadResources"; }

}
