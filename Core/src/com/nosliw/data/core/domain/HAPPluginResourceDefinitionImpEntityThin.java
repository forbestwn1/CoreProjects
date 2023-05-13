package com.nosliw.data.core.domain;

import com.nosliw.data.core.resource.HAPPluginResourceDefinitionImp;
import com.nosliw.data.core.runtime.HAPRuntimeEnvironment;

public class HAPPluginResourceDefinitionImpEntityThin extends HAPPluginResourceDefinitionImp{

	public HAPPluginResourceDefinitionImpEntityThin(String entityType, HAPRuntimeEnvironment runtimeEnv) {
		super(entityType, runtimeEnv);
	}

	@Override
	protected HAPIdEntityInDomain parseEntity(Object content, HAPContextParser parserContext) {
		HAPIdEntityInDomain out = this.getRuntimeEnvironment().getDomainEntityDefinitionManager().parseDefinition(getResourceType(), content, parserContext);
		return out;
	}

}
