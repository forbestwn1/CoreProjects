package com.nosliw.data.core.domain.entity.container;

import org.json.JSONObject;

import com.nosliw.common.utils.HAPConstantShared;
import com.nosliw.data.core.domain.HAPContextParser;
import com.nosliw.data.core.domain.HAPIdEntityInDomain;
import com.nosliw.data.core.domain.HAPPluginEntityDefinitionInDomainImpComplex;
import com.nosliw.data.core.runtime.HAPRuntimeEnvironment;

public class HAPPluginEntityDefinitionInDomainContainer extends HAPPluginEntityDefinitionInDomainImpComplex{

	public HAPPluginEntityDefinitionInDomainContainer(HAPRuntimeEnvironment runtimeEnv) {
		super(HAPConstantShared.RUNTIME_RESOURCE_TYPE_COMPLEXCONTAINER, HAPDefinitionEntityComplexContainer.class, runtimeEnv);
	}

	@Override
	protected void parseComplexDefinitionContent(HAPIdEntityInDomain entityId, JSONObject jsonObj, HAPContextParser parserContext) {
	}

}
