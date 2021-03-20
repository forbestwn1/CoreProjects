package com.nosliw.data.core.script.context.resource;

import com.nosliw.common.utils.HAPConstantShared;
import com.nosliw.data.core.resource.HAPPluginResourceDefinition;
import com.nosliw.data.core.resource.HAPResourceDefinition;
import com.nosliw.data.core.resource.HAPResourceIdSimple;

public class HAPPluginResourceDefinitionContext implements HAPPluginResourceDefinition{

	@Override
	public String getResourceType() {   return HAPConstantShared.RUNTIME_RESOURCE_TYPE_CONTEXT;  }

	@Override
	public HAPResourceDefinition getResource(HAPResourceIdSimple resourceId) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public HAPResourceDefinition parseResourceDefinition(Object content) {
		// TODO Auto-generated method stub
		return null;
	}

}
