package com.nosliw.data.core.domain.entity.script;

import com.nosliw.common.utils.HAPConstantShared;
import com.nosliw.data.core.domain.HAPContextParser;
import com.nosliw.data.core.domain.HAPIdEntityInDomain;
import com.nosliw.data.core.domain.definition.HAPPluginEntityDefinitionInDomainImpSimple;
import com.nosliw.data.core.runtime.HAPRuntimeEnvironment;

public class HAPPluginEntityDefinitionInDomainScript extends HAPPluginEntityDefinitionInDomainImpSimple{

	public HAPPluginEntityDefinitionInDomainScript(HAPRuntimeEnvironment runtimeEnv) {
		super(HAPConstantShared.RUNTIME_RESOURCE_TYPE_SCRIPT, HAPDefinitionEntityScript.class, runtimeEnv);
	}
	
	@Override
	protected void parseDefinitionContentJson(HAPIdEntityInDomain entityId, Object jsonObj, HAPContextParser parserContext) {
		HAPDefinitionEntityScript entity = (HAPDefinitionEntityScript)this.getEntity(entityId, parserContext);
		entity.setScript(jsonObj+"");
	}

	@Override
	protected void parseDefinitionContentJavascript(HAPIdEntityInDomain entityId, Object jsObj, HAPContextParser parserContext) {
		HAPDefinitionEntityScript entity = (HAPDefinitionEntityScript)this.getEntity(entityId, parserContext);
		entity.setScript(jsObj+"");
	}
}
