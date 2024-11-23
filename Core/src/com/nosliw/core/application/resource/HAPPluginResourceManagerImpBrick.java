package com.nosliw.core.application.resource;

import com.nosliw.core.application.HAPBundle;
import com.nosliw.core.application.HAPUtilityBundle;
import com.nosliw.data.core.resource.HAPPluginResourceManager;
import com.nosliw.data.core.resource.HAPResourceDataOrWrapper;
import com.nosliw.data.core.resource.HAPResourceIdSimple;
import com.nosliw.data.core.runtime.HAPRuntimeEnvironment;
import com.nosliw.data.core.runtime.HAPRuntimeInfo;

public class HAPPluginResourceManagerImpBrick implements HAPPluginResourceManager{

	private HAPRuntimeEnvironment m_runtimeEnv;
	
	public HAPPluginResourceManagerImpBrick(HAPRuntimeEnvironment runtimeEnv) {
		this.m_runtimeEnv = runtimeEnv;
	}

	@Override
	public HAPResourceDataOrWrapper getResourceData(HAPResourceIdSimple simpleResourceId, HAPRuntimeInfo runtimeInfo) {
		HAPBundle bundle = HAPUtilityBundle.getBrickBundle(simpleResourceId, this.m_runtimeEnv.getBrickManager()); 
		return new HAPWrapperResourceDataBrick(bundle, this.m_runtimeEnv);
	}
}
