package com.nosliw.data.core.runtime;

import com.nosliw.common.constant.HAPAttribute;
import com.nosliw.common.constant.HAPEntityWithAttribute;

@HAPEntityWithAttribute
public class HAPResourceDiscovered {

	@HAPAttribute
	public static final String RESOURCEINFO = "resourceInfo";
	@HAPAttribute
	public static final String RESOURCE = "resource";

	private HAPResourceInfo m_resourceInfo;
	
	private HAPResource m_resource;
	
	public HAPResourceDiscovered(HAPResourceInfo resourceInfo, HAPResource resource){
		this.m_resourceInfo = resourceInfo;
		this.m_resource = resource;
	}
	
	public HAPResourceInfo getResourceInfo(){ return this.m_resourceInfo;  }
	
	public HAPResource getResource(){ return this.m_resource;  }
	
}
