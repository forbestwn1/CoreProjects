package com.nosliw.data.core.domain.definition;

import com.nosliw.core.application.division.manual.HAPManualEntity;
import com.nosliw.data.core.runtime.HAPRuntimeEnvironment;

public abstract class HAPPluginEntityDefinitionInDomainImpSimple extends HAPPluginEntityDefinitionInDomainImp{

	public HAPPluginEntityDefinitionInDomainImpSimple(String entityType, Class<? extends HAPManualEntity> entityClass, HAPRuntimeEnvironment runtimeEnv) {
		super(entityType, entityClass, runtimeEnv);
	}
	
	@Override
	public boolean isComplexEntity() {    return false;    }
}
