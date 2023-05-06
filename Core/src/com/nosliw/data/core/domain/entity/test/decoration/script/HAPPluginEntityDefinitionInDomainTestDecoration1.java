package com.nosliw.data.core.domain.entity.test.decoration.script;

import com.nosliw.data.core.domain.HAPContextParser;
import com.nosliw.data.core.domain.HAPIdEntityInDomain;
import com.nosliw.data.core.domain.HAPPluginEntityDefinitionInDomainImp;
import com.nosliw.data.core.runtime.HAPRuntimeEnvironment;

public class HAPPluginEntityDefinitionInDomainTestDecoration1 extends HAPPluginEntityDefinitionInDomainImp{

	public HAPPluginEntityDefinitionInDomainTestDecoration1(HAPRuntimeEnvironment runtimeEnv) {
		super(HAPDefinitionEntityTestDecoration1.class, runtimeEnv);
	}
	
	@Override
	protected void parseDefinitionContent(HAPIdEntityInDomain entityId, Object obj, HAPContextParser parserContext) {
		
		String scriptName = (String)jsonObj.opt(HAPDefinitionEntityTestDecoration1.ATTR_SCRIPTNAME);
		entity.setScriptName(scriptName);
		
		HAPDefinitionEntityTestDecoration1 entity = (HAPDefinitionEntityTestDecoration1)this.getEntity(entityId, parserContext);
		entity.setScript(obj+"");
	}

	@Override
	public boolean isComplexEntity() {   return true;  }
}
