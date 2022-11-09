package com.nosliw.data.core.domain;

import com.nosliw.data.core.domain.entity.HAPDefinitionEntityInDomain;
import com.nosliw.data.core.runtime.HAPRuntimeEnvironment;

public abstract class HAPPluginEntityDefinitionInDomainImpSimple extends HAPPluginEntityDefinitionInDomainImp{

	public HAPPluginEntityDefinitionInDomainImpSimple(Class<? extends HAPDefinitionEntityInDomain> entityClass, HAPRuntimeEnvironment runtimeEnv) {
		super(entityClass, runtimeEnv);
	}
	
	@Override
	public boolean isComplexEntity() {    return false;    }
}
