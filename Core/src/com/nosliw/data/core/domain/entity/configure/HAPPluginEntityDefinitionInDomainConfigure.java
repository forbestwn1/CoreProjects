package com.nosliw.data.core.domain.entity.configure;

import com.nosliw.data.core.domain.HAPContextParser;
import com.nosliw.data.core.domain.HAPIdEntityInDomain;
import com.nosliw.data.core.domain.HAPPluginEntityDefinitionInDomainImp;
import com.nosliw.data.core.runtime.HAPRuntimeEnvironment;

public class HAPPluginEntityDefinitionInDomainConfigure extends HAPPluginEntityDefinitionInDomainImp{

	public HAPPluginEntityDefinitionInDomainConfigure(HAPRuntimeEnvironment runtimeEnv) {
		super(HAPDefinitionEntityConfigure.class, runtimeEnv);
	}
	
	@Override
	protected void parseDefinitionContent(HAPIdEntityInDomain entityId, Object obj, HAPContextParser parserContext) {
		HAPDefinitionEntityConfigure entity = (HAPDefinitionEntityConfigure)this.getEntity(entityId, parserContext);
		entity.setScript(obj+"");
	}

	@Override
	public boolean isComplexEntity() {   return false;  }
}
