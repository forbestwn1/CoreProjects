package com.nosliw.data.core.domain.entity.uipage;

import com.nosliw.common.utils.HAPConstantShared;
import com.nosliw.data.core.domain.entity.HAPDefinitionEntityInDomain;
import com.nosliw.data.core.runtime.HAPRuntimeEnvironment;

public class HAPPluginEntityDefinitionInDomainUITag extends HAPPluginEntityDefinitionInDomainUIUnit{

	public HAPPluginEntityDefinitionInDomainUITag(String entityType, Class<? extends HAPDefinitionEntityInDomain> entityClass, HAPRuntimeEnvironment runtimeEnv) {
		super(HAPConstantShared.RUNTIME_RESOURCE_TYPE_UITAG, HAPDefinitionEntityComplexUITag.class, runtimeEnv);
	}

	
}
