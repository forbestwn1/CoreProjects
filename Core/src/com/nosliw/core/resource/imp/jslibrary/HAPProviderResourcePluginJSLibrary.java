package com.nosliw.core.resource.imp.jslibrary;

import org.springframework.stereotype.Component;

import com.nosliw.common.utils.HAPConstantShared;
import com.nosliw.core.resource.HAPFactoryResourceTypeId;
import com.nosliw.core.resource.HAPProviderResourcePluginImp;

@Component
public class HAPProviderResourcePluginJSLibrary extends HAPProviderResourcePluginImp{

	public HAPProviderResourcePluginJSLibrary() {
		this.registerPlugin(HAPFactoryResourceTypeId.newInstance(HAPConstantShared.RUNTIME_RESOURCE_TYPE_JSLIBRARY), new HAPPluginResourceManagerJSLibrary());
	}
}
