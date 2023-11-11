package com.nosliw.data.core.domain.entity.value;

import com.nosliw.common.utils.HAPConstantShared;
import com.nosliw.data.core.domain.HAPContextParser;
import com.nosliw.data.core.domain.HAPIdEntityInDomain;
import com.nosliw.data.core.domain.HAPPluginEntityDefinitionInDomainImpSimple;
import com.nosliw.data.core.runtime.HAPRuntimeEnvironment;

public class HAPPluginEntityDefinitionInDomainValue extends HAPPluginEntityDefinitionInDomainImpSimple{

	public HAPPluginEntityDefinitionInDomainValue(HAPRuntimeEnvironment runtimeEnv) {
		super(HAPConstantShared.RUNTIME_RESOURCE_TYPE_VALUE, HAPDefinitionEntityValue.class, runtimeEnv);
	}
	
	@Override
	protected void parseDefinitionContentJson(HAPIdEntityInDomain entityId, Object jsonValue, HAPContextParser parserContext) {
		HAPDefinitionEntityValue entity = (HAPDefinitionEntityValue)this.getEntity(entityId, parserContext);
		entity.setValue(jsonValue);
	}
}
