package com.nosliw.core.application;

import com.nosliw.core.resource.HAPManagerResource;
import com.nosliw.core.resource.HAPPluginResourceManager;
import com.nosliw.core.resource.HAPResourceDataOrWrapper;
import com.nosliw.core.resource.HAPResourceIdSimple;
import com.nosliw.core.runtime.HAPRuntimeInfo;

public class HAPPluginResourceManagerImpBrick implements HAPPluginResourceManager{

	private HAPManagerResource m_resourceManager;
	private HAPManagerApplicationBrick m_brickManager;
	
	public HAPPluginResourceManagerImpBrick(HAPManagerResource resourceManager, HAPManagerApplicationBrick brickManager) {
		this.m_resourceManager = resourceManager;
		this.m_brickManager = brickManager;
	}

	@Override
	public HAPResourceDataOrWrapper getResourceData(HAPResourceIdSimple simpleResourceId, HAPRuntimeInfo runtimeInfo) {
		HAPBundle bundle = HAPUtilityBundle.getBrickBundle(simpleResourceId, this.m_brickManager, runtimeInfo); 
		return new HAPWrapperResourceDataBrick(bundle, runtimeInfo, this.m_resourceManager);
	}
}
