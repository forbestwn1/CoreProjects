package com.nosliw.core.application.resource;

import com.nosliw.core.application.HAPBundle;
import com.nosliw.core.application.HAPManagerApplicationBrick;
import com.nosliw.data.core.resource.HAPPluginResourceManager;
import com.nosliw.data.core.resource.HAPResourceDataOrWrapper;
import com.nosliw.data.core.resource.HAPResourceIdSimple;
import com.nosliw.data.core.runtime.HAPRuntimeInfo;

public class HAPPluginResourceManagerImpBrick implements HAPPluginResourceManager{

	private HAPManagerApplicationBrick m_brickMan;
	
	public HAPPluginResourceManagerImpBrick(HAPManagerApplicationBrick brickMan) {
		this.m_brickMan = brickMan;
	}

	@Override
	public HAPResourceDataOrWrapper getResourceData(HAPResourceIdSimple simpleResourceId, HAPRuntimeInfo runtimeInfo) {
		HAPBundle bundle = this.m_brickMan.getBrickBundle(simpleResourceId);
		return new HAPWrapperResourceDataBrick(bundle, this.m_brickMan);
	}
}
