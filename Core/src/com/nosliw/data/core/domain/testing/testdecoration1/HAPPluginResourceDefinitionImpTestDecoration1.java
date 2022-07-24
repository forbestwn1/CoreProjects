package com.nosliw.data.core.domain.testing.testdecoration1;

import com.nosliw.data.core.domain.HAPContextParser;
import com.nosliw.data.core.domain.HAPIdEntityInDomain;
import com.nosliw.data.core.resource.HAPPluginResourceDefinitionImp;
import com.nosliw.data.core.runtime.HAPRuntimeEnvironment;

public class HAPPluginResourceDefinitionImpTestDecoration1 extends HAPPluginResourceDefinitionImp{

	public HAPPluginResourceDefinitionImpTestDecoration1(String resourceType, HAPRuntimeEnvironment runtimeEnv) {
		super(resourceType, runtimeEnv);
	}

	@Override
	protected HAPIdEntityInDomain parseEntity(Object content, HAPContextParser parserContext) {
		HAPDefinitionEntityTestDecoration1 entity = new HAPDefinitionEntityTestDecoration1((String)content);
		return parserContext.getCurrentDomain().addEntity(entity);
	}

}
