package com.nosliw.data.core.domain.definition;

import com.nosliw.core.application.division.manual.HAPManualBrick;
import com.nosliw.data.core.runtime.HAPRuntimeEnvironment;

public abstract class HAPPluginEntityDefinitionInDomainImpSimple extends HAPPluginEntityDefinitionInDomainImp{

	public HAPPluginEntityDefinitionInDomainImpSimple(String entityType, Class<? extends HAPManualBrick> entityClass, HAPRuntimeEnvironment runtimeEnv) {
		super(entityType, entityClass, runtimeEnv);
	}
	
	@Override
	public boolean isComplexEntity() {    return false;    }
}
