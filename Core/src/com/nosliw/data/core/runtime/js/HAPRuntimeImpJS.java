package com.nosliw.data.core.runtime.js;

import com.nosliw.data.core.runtime.HAPResourceDiscovery;
import com.nosliw.data.core.runtime.HAPResourceManager;
import com.nosliw.data.core.runtime.HAPRuntimeImp;

public abstract class HAPRuntimeImpJS extends HAPRuntimeImp{

	private HAPResourceDiscovery m_resourceDiscovery;
	
	private HAPResourceManager m_resourceManager;
	
	public HAPRuntimeImpJS(HAPResourceDiscovery resourceDiscovery, HAPResourceManager resourceMan){
		super();
		this.m_resourceDiscovery = resourceDiscovery;
		this.m_resourceManager = resourceMan;
	}
	
	@Override
	public HAPResourceManager getResourceManager() {		return this.m_resourceManager;	}

	@Override
	public HAPResourceDiscovery getResourceDiscovery() {		return this.m_resourceDiscovery;	}
	
}
