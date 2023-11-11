package com.nosliw.data.core.domain.entity.service.provider;

import com.nosliw.common.serialization.HAPSerializationFormat;
import com.nosliw.common.utils.HAPConstantShared;
import com.nosliw.data.core.domain.HAPContextParser;
import com.nosliw.data.core.domain.HAPIdEntityInDomain;
import com.nosliw.data.core.domain.HAPPluginEntityDefinitionInDomainImpSimple;
import com.nosliw.data.core.runtime.HAPRuntimeEnvironment;

public class HAPPluginEntityDefinitionInDomainServiceProvider extends HAPPluginEntityDefinitionInDomainImpSimple{

	public HAPPluginEntityDefinitionInDomainServiceProvider(HAPRuntimeEnvironment runtimeEnv) {
		super(HAPConstantShared.RUNTIME_RESOURCE_TYPE_SERVICEPROVIDER, HAPDefinitionEntityInDomainServiceProvider.class, runtimeEnv);
	}
	
	@Override
	protected void parseDefinitionContentJson(HAPIdEntityInDomain entityId, Object jsonValue, HAPContextParser parserContext) {
		HAPDefinitionEntityInDomainServiceProvider entity = (HAPDefinitionEntityInDomainServiceProvider)this.getEntity(entityId, parserContext);
		entity.buildObject(jsonValue, HAPSerializationFormat.JSON);
	}
}
