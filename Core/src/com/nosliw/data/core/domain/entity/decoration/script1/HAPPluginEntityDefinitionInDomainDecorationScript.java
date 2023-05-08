package com.nosliw.data.core.domain.entity.decoration.script1;

import org.json.JSONObject;

import com.nosliw.data.core.domain.HAPContextParser;
import com.nosliw.data.core.domain.HAPIdEntityInDomain;
import com.nosliw.data.core.domain.HAPPluginEntityDefinitionInDomainImpComplex;
import com.nosliw.data.core.runtime.HAPRuntimeEnvironment;

public class HAPPluginEntityDefinitionInDomainDecorationScript extends HAPPluginEntityDefinitionInDomainImpComplex{

	public HAPPluginEntityDefinitionInDomainDecorationScript(HAPRuntimeEnvironment runtimeEnv) {
		super(HAPDefinitionEntityDecorationScript.class, runtimeEnv);
	}
	
	@Override
	protected void parseComplexDefinitionContent(HAPIdEntityInDomain entityId, JSONObject jsonObj, HAPContextParser parserContext) {
		HAPDefinitionEntityDecorationScript entity = (HAPDefinitionEntityDecorationScript)this.getEntity(entityId, parserContext);
		String scriptName = (String)jsonObj.opt(HAPDefinitionEntityDecorationScript.ATTR_SCRIPTNAME);
		entity.setScriptName(scriptName);
	}
}
