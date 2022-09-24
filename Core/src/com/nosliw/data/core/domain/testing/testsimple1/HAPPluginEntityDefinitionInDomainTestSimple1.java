package com.nosliw.data.core.domain.testing.testsimple1;

import org.json.JSONObject;

import com.nosliw.common.utils.HAPConstantShared;
import com.nosliw.data.core.domain.HAPContextParser;
import com.nosliw.data.core.domain.HAPIdEntityInDomain;
import com.nosliw.data.core.domain.HAPPluginEntityDefinitionInDomainImp;
import com.nosliw.data.core.resource.HAPFactoryResourceId;
import com.nosliw.data.core.resource.HAPResourceDefinition;
import com.nosliw.data.core.runtime.HAPRuntimeEnvironment;

public class HAPPluginEntityDefinitionInDomainTestSimple1 extends HAPPluginEntityDefinitionInDomainImp{

	public HAPPluginEntityDefinitionInDomainTestSimple1(HAPRuntimeEnvironment runtimeEnv) {
		super(HAPDefinitionEntityTestSimple1.class, runtimeEnv);
	}
	
	@Override
	protected void parseDefinitionContent(HAPIdEntityInDomain entityId, Object obj, HAPContextParser parserContext) {
		HAPDefinitionEntityTestSimple1 entity = (HAPDefinitionEntityTestSimple1)this.getEntity(entityId, parserContext);
		JSONObject jsonObj = (JSONObject)obj;
		String scriptName = (String)jsonObj.opt(HAPDefinitionEntityTestSimple1.ATTR_SCRIPTNAME);
		entity.setScriptName(scriptName);

		HAPResourceDefinition scriptResoureDef = this.getRuntimeEnvironment().getResourceDefinitionManager().getResourceDefinition(HAPFactoryResourceId.newInstance(HAPConstantShared.RUNTIME_RESOURCE_TYPE_SCRIPT, scriptName), parserContext.getGlobalDomain());
		entity.setSimpleAttribute(HAPDefinitionEntityTestSimple1.ATTR_SCRIPT, scriptResoureDef.getEntityId());
	}

	@Override
	public boolean isComplexEntity() {	return false;	}
}
