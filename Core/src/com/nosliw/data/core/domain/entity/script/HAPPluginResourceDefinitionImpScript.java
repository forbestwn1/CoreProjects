package com.nosliw.data.core.domain.entity.script;

import com.nosliw.data.core.domain.HAPContextParser;
import com.nosliw.data.core.domain.HAPIdEntityInDomain;
import com.nosliw.data.core.resource.HAPPluginResourceDefinitionImp;
import com.nosliw.data.core.runtime.HAPRuntimeEnvironment;

public class HAPPluginResourceDefinitionImpScript extends HAPPluginResourceDefinitionImp{

	public HAPPluginResourceDefinitionImpScript(HAPRuntimeEnvironment runtimeEnv) {
		super(HAPDefinitionEntityScript.ENTITY_TYPE, runtimeEnv);
	}

	@Override
	protected HAPIdEntityInDomain parseEntity(Object content, HAPContextParser parserContext) {
		HAPIdEntityInDomain out = this.getRuntimeEnvironment().getDomainEntityManager().parseDefinition(getResourceType(), content, parserContext);
		return out;
	}

}
