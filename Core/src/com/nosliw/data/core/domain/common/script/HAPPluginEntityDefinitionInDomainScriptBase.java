package com.nosliw.data.core.domain.common.script;

import com.nosliw.data.core.domain.HAPContextParser;
import com.nosliw.data.core.domain.HAPIdEntityInDomain;
import com.nosliw.data.core.domain.HAPPluginEntityDefinitionInDomainImp;
import com.nosliw.data.core.resource.HAPResourceId;
import com.nosliw.data.core.runtime.HAPRuntimeEnvironment;

public class HAPPluginEntityDefinitionInDomainScriptBase extends HAPPluginEntityDefinitionInDomainImp{

	private boolean m_isComplex = false;
	
	public HAPPluginEntityDefinitionInDomainScriptBase(String entityType, boolean isComplex, HAPRuntimeEnvironment runtimeEnv) {
		super(entityType, isComplex?HAPDefinitionEntityScriptComplex.class:HAPDefinitionEntityScriptSimple.class, runtimeEnv);
		this.m_isComplex = isComplex;
	}
	
	@Override
	protected void parseDefinitionContent(HAPIdEntityInDomain entityId, Object obj, HAPContextParser parserContext) {
		HAPDefinitionEntityScript entity = (HAPDefinitionEntityScript)this.getEntity(entityId, parserContext);
		entity.setScript(obj+"");
	}

	@Override
	protected void postParseDefinitionContent(HAPIdEntityInDomain entityId, HAPContextParser parserContext) {
		HAPResourceId resourceId = this.getEntityDefinitionInfo(entityId, parserContext).getResourceId();
		if(resourceId!=null) {
			HAPDefinitionEntityScript entity = (HAPDefinitionEntityScript)this.getEntity(entityId, parserContext);
			entity.setScriptResourceId(resourceId);
		}
	}
	
	@Override
	public boolean isComplexEntity() {   return this.m_isComplex;  }
}
