package com.nosliw.core.application;

import com.nosliw.common.path.HAPPath;
import com.nosliw.core.resource.HAPManagerResource;
import com.nosliw.core.resource.HAPResourceData;
import com.nosliw.core.resource.HAPResourceDataOrWrapper;
import com.nosliw.core.resource.HAPWrapperResourceDataImp;
import com.nosliw.core.runtime.HAPRuntimeInfo;

public class HAPWrapperResourceDataBrick extends HAPWrapperResourceDataImp{

	private HAPBundle m_bundle;
	
	private HAPManagerResource m_resourceManager;
	
	private HAPRuntimeInfo m_runtimeInfo;
	
	public HAPWrapperResourceDataBrick(HAPBundle bundle, HAPRuntimeInfo runtimeInfo, HAPManagerResource resourceManager) {
		this.m_bundle = bundle;
		this.m_runtimeInfo = runtimeInfo;
		this.m_resourceManager = resourceManager;
	}
	
	@Override
	public HAPResourceDataOrWrapper getDescendant(HAPPath path) {
		if(path.getLength()>=2) {
			throw new RuntimeException();
		}
		return HAPUtilityBrickResource.getExportResourceData(this.m_bundle, path.toString(), this.m_resourceManager, this.m_runtimeInfo);
	}

	@Override
	public HAPResourceData getResourceData() {
		return HAPUtilityBrickResource.getExportResourceData(this.m_bundle, null, this.m_resourceManager, this.m_runtimeInfo);
	}

}
