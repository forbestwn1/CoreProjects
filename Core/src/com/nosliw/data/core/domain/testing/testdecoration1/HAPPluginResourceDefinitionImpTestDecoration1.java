package com.nosliw.data.core.domain.testing.testdecoration1;

import com.nosliw.data.core.domain.HAPContextParser;
import com.nosliw.data.core.domain.HAPIdEntityInDomain;
import com.nosliw.data.core.resource.HAPPluginResourceDefinitionImp;
import com.nosliw.data.core.runtime.HAPRuntimeEnvironment;

public class HAPPluginResourceDefinitionImpTestDecoration1 extends HAPPluginResourceDefinitionImp{

	public HAPPluginResourceDefinitionImpTestDecoration1(HAPRuntimeEnvironment runtimeEnv) {
		super(HAPDefinitionEntityTestDecoration1.ENTITY_TYPE, runtimeEnv);
	}

	@Override
	protected HAPIdEntityInDomain parseEntity(Object content, HAPContextParser parserContext) {
		HAPIdEntityInDomain out = this.getRuntimeEnvironment().getDomainEntityManager().parseDefinition(getResourceType(), content, parserContext);
		return out;
	}

}
