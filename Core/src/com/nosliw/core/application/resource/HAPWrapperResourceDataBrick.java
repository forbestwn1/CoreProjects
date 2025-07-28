package com.nosliw.core.application.resource;

import com.nosliw.common.path.HAPPath;
import com.nosliw.core.application.HAPBundle;
import com.nosliw.core.resource.HAPResourceData;
import com.nosliw.core.resource.HAPResourceDataOrWrapper;
import com.nosliw.core.resource.HAPWrapperResourceDataImp;
import com.nosliw.data.core.runtime.HAPRuntimeEnvironment;

public class HAPWrapperResourceDataBrick extends HAPWrapperResourceDataImp{

	private HAPBundle m_bundle;
	
	private HAPRuntimeEnvironment m_runtimeEnv;
	
	public HAPWrapperResourceDataBrick(HAPBundle bundle, HAPRuntimeEnvironment runtimeEnv) {
		this.m_bundle = bundle;
		this.m_runtimeEnv = runtimeEnv;
	}
	
	@Override
	public HAPResourceDataOrWrapper getDescendant(HAPPath path) {
		if(path.getLength()>=2) {
			throw new RuntimeException();
		}
		return this.m_bundle.getExportResourceData(path.toString(), this.m_runtimeEnv.getResourceManager(), this.m_runtimeEnv.getRuntime().getRuntimeInfo());
	}

	@Override
	public HAPResourceData getResourceData() {
		return this.m_bundle.getExportResourceData(null, this.m_runtimeEnv.getResourceManager(), this.m_runtimeEnv.getRuntime().getRuntimeInfo());
	}

}
