package com.nosliw.data.core.domain.entity.script;

import com.nosliw.data.core.domain.HAPContextParser;
import com.nosliw.data.core.domain.HAPIdEntityInDomain;
import com.nosliw.data.core.domain.HAPPluginEntityDefinitionInDomainImp;
import com.nosliw.data.core.runtime.HAPRuntimeEnvironment;

public class HAPPluginEntityDefinitionInDomainScript extends HAPPluginEntityDefinitionInDomainImp{

	public HAPPluginEntityDefinitionInDomainScript(HAPRuntimeEnvironment runtimeEnv) {
		super(HAPDefinitionEntityScript.class, runtimeEnv);
	}
	
	@Override
	protected void parseDefinitionContent(HAPIdEntityInDomain entityId, Object obj, HAPContextParser parserContext) {
		HAPDefinitionEntityScript entity = (HAPDefinitionEntityScript)this.getEntity(entityId, parserContext);
		entity.setScript(obj+"");
	}

	@Override
	public boolean isComplexEntity() {   return false;  }
}
