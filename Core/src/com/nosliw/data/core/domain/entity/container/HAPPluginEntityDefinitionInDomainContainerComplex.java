package com.nosliw.data.core.domain.entity.container;

import org.json.JSONObject;

import com.nosliw.common.utils.HAPConstantShared;
import com.nosliw.data.core.domain.HAPContextParser;
import com.nosliw.data.core.domain.HAPIdEntityInDomain;
import com.nosliw.data.core.domain.HAPPluginEntityDefinitionInDomainImpComplexJson;
import com.nosliw.data.core.runtime.HAPRuntimeEnvironment;

public class HAPPluginEntityDefinitionInDomainContainerComplex extends HAPPluginEntityDefinitionInDomainImpComplexJson{

	public HAPPluginEntityDefinitionInDomainContainerComplex(HAPRuntimeEnvironment runtimeEnv) {
		super(HAPConstantShared.RUNTIME_RESOURCE_TYPE_CONTAINERCOMPLEX, HAPDefinitionEntityContainerComplex.class, runtimeEnv);
	}

	@Override
	protected void setupAttributeForComplexEntity(HAPIdEntityInDomain entityId, HAPContextParser parserContext) {	}
	
	@Override
	protected void parseComplexDefinitionContentJson(HAPIdEntityInDomain entityId, JSONObject jsonObj, HAPContextParser parserContext) {
	}

}