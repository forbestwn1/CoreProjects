package com.nosliw.data.core.domain.entity.configure;

import com.nosliw.data.core.domain.HAPContextParser;
import com.nosliw.data.core.domain.HAPIdEntityInDomain;
import com.nosliw.data.core.resource.HAPPluginResourceDefinitionImp;
import com.nosliw.data.core.runtime.HAPRuntimeEnvironment;

public class HAPPluginResourceDefinitionImpConfigure extends HAPPluginResourceDefinitionImp{

	public HAPPluginResourceDefinitionImpConfigure(HAPRuntimeEnvironment runtimeEnv) {
		super(HAPDefinitionEntityConfigure.ENTITY_TYPE, runtimeEnv);
	}

	@Override
	protected HAPIdEntityInDomain parseEntity(Object content, HAPContextParser parserContext) {
		HAPIdEntityInDomain out = this.getRuntimeEnvironment().getDomainEntityManager().parseDefinition(getResourceType(), content, parserContext);
		return out;
	}

}
