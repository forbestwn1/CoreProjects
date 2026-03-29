package com.nosliw.core.application.entity.configure;

import java.util.Map;

import org.springframework.stereotype.Component;

import com.nosliw.common.utils.HAPConstantShared;
import com.nosliw.core.resource.HAPFactoryResourceTypeId;
import com.nosliw.core.resource.HAPIdResourceType;
import com.nosliw.core.resource.HAPPluginResourceManager;
import com.nosliw.core.resource.HAPProviderResourcePluginImp;

@Component
public class HAPProviderResourcePluginConfigure extends HAPProviderResourcePluginImp{

	@Override
	public Map<HAPIdResourceType, HAPPluginResourceManager> getResourceManagerPlugins() {
		if(super.getResourceManagerPlugins().isEmpty()) {
			this.registerPlugin(HAPFactoryResourceTypeId.newInstance(HAPConstantShared.RUNTIME_RESOURCE_TYPE_CONFIGURE), new HAPPluginResourceManagerConfigure());
		}
		return super.getResourceManagerPlugins();
	}

}
