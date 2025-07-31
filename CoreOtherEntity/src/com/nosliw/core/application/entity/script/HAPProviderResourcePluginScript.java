package com.nosliw.core.application.entity.script;

import org.springframework.stereotype.Component;

import com.nosliw.common.utils.HAPConstantShared;
import com.nosliw.core.resource.HAPFactoryResourceTypeId;
import com.nosliw.core.resource.HAPProviderResourcePluginImp;

@Component
public class HAPProviderResourcePluginScript extends HAPProviderResourcePluginImp{

	public HAPProviderResourcePluginScript() {
		this.registerPlugin(HAPFactoryResourceTypeId.newInstance(HAPConstantShared.RUNTIME_RESOURCE_TYPE_SCRIPT), new HAPPluginResourceManagerScript(HAPConstantShared.RUNTIME_RESOURCE_TYPE_SCRIPT));
	}
	
}
