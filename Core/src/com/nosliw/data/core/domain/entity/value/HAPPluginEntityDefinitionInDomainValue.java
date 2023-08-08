package com.nosliw.data.core.domain.entity.value;

import com.nosliw.common.utils.HAPConstantShared;
import com.nosliw.data.core.domain.HAPContextParser;
import com.nosliw.data.core.domain.HAPIdEntityInDomain;
import com.nosliw.data.core.domain.HAPPluginEntityDefinitionInDomainImp;
import com.nosliw.data.core.runtime.HAPRuntimeEnvironment;

public class HAPPluginEntityDefinitionInDomainValue extends HAPPluginEntityDefinitionInDomainImp{

	public HAPPluginEntityDefinitionInDomainValue(HAPRuntimeEnvironment runtimeEnv) {
		super(HAPConstantShared.RUNTIME_RESOURCE_TYPE_VALUE, HAPDefinitionEntityValue.class, runtimeEnv);
	}
	
	@Override
	protected void parseDefinitionContent(HAPIdEntityInDomain entityId, Object obj, HAPContextParser parserContext) {
		HAPDefinitionEntityValue entity = (HAPDefinitionEntityValue)this.getEntity(entityId, parserContext);
		entity.setValue(obj);
	}

	@Override
	public boolean isComplexEntity() {   return false;  }
}
