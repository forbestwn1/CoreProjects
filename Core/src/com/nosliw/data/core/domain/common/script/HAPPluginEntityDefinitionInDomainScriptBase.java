package com.nosliw.data.core.domain.common.script;

import com.nosliw.data.core.domain.HAPContextParser;
import com.nosliw.data.core.domain.HAPIdEntityInDomain;
import com.nosliw.data.core.domain.HAPPluginEntityDefinitionInDomainImp;
import com.nosliw.data.core.runtime.HAPRuntimeEnvironment;

public class HAPPluginEntityDefinitionInDomainScriptBase extends HAPPluginEntityDefinitionInDomainImp{

	private boolean m_isComplex = false;
	
	public HAPPluginEntityDefinitionInDomainScriptBase(String entityType, boolean isComplex, HAPRuntimeEnvironment runtimeEnv) {
		super(entityType, isComplex?HAPDefinitionEntityScriptComplex.class:HAPDefinitionEntityScript.class, runtimeEnv);
		this.m_isComplex = isComplex;
	}
	
	@Override
	protected void parseDefinitionContent(HAPIdEntityInDomain entityId, Object obj, HAPContextParser parserContext) {
		if(this.m_isComplex) {
			HAPDefinitionEntityScriptComplex entity = (HAPDefinitionEntityScriptComplex)this.getEntity(entityId, parserContext);
			entity.setScript(obj+"");
		}
		else {
			HAPDefinitionEntityScript entity = (HAPDefinitionEntityScript)this.getEntity(entityId, parserContext);
			entity.setScript(obj+"");
		}
	}

	@Override
	public boolean isComplexEntity() {   return false;  }
}
