package com.nosliw.core.application.entity.configure;

import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;

import com.nosliw.common.utils.HAPConstantShared;
import com.nosliw.core.application.HAPManagerApplicationBrick;
import com.nosliw.core.resource.HAPFactoryResourceTypeId;
import com.nosliw.core.resource.HAPIdResourceType;
import com.nosliw.core.resource.HAPManagerResource;
import com.nosliw.core.resource.HAPPluginResourceManager;
import com.nosliw.core.resource.HAPProviderResourcePluginImp;

public class HAPProviderResourcePluginConfigure extends HAPProviderResourcePluginImp{

	private HAPManagerResource m_resourceManager;
	private HAPManagerApplicationBrick m_brickManager;
	
	@Override
	public Map<HAPIdResourceType, HAPPluginResourceManager> getResourceManagerPlugins() {
		if(super.getResourceManagerPlugins().isEmpty()) {
			this.registerPlugin(HAPFactoryResourceTypeId.newInstance(HAPConstantShared.RUNTIME_RESOURCE_TYPE_CONFIGURE), new HAPPluginResourceManagerConfigure());
		}
		return super.getResourceManagerPlugins();
	}

	@Autowired
	private void setBrickManager(HAPManagerApplicationBrick brickManager) {
		this.m_brickManager = brickManager;		
	}
	
	@Autowired
	private void setResourceManager(HAPManagerResource resourceManager) {
		this.m_resourceManager = resourceManager;	
	}

}
