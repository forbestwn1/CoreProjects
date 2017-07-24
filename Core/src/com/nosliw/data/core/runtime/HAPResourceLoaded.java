package com.nosliw.data.core.runtime;

public class HAPResourceLoaded {

	private HAPResourceInfo m_resourceInfo;
	
	private HAPResource m_resource;
	
	public HAPResourceLoaded(HAPResourceInfo resourceInfo, HAPResource resource){
		this.m_resourceInfo = resourceInfo;
		this.m_resource = resource;
	}
	
	public HAPResourceInfo getResourceInfo(){ return this.m_resourceInfo;  }
	
	public HAPResource getResource(){ return this.m_resource;  }
	
}
