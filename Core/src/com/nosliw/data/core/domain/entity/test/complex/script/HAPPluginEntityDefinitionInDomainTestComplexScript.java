package com.nosliw.data.core.domain.entity.test.complex.script;

import org.json.JSONObject;

import com.nosliw.common.utils.HAPConstantShared;
import com.nosliw.data.core.domain.HAPContextParser;
import com.nosliw.data.core.domain.HAPIdEntityInDomain;
import com.nosliw.data.core.domain.HAPPluginEntityDefinitionInDomainImpComplex;
import com.nosliw.data.core.domain.HAPUtilityEntityDefinition;
import com.nosliw.data.core.resource.HAPFactoryResourceId;
import com.nosliw.data.core.resource.HAPResourceDefinition;
import com.nosliw.data.core.runtime.HAPRuntimeEnvironment;

public class HAPPluginEntityDefinitionInDomainTestComplexScript extends HAPPluginEntityDefinitionInDomainImpComplex{

	public HAPPluginEntityDefinitionInDomainTestComplexScript(HAPRuntimeEnvironment runtimeEnv) {
		super(HAPDefinitionEntityTestComplexScript.class, runtimeEnv);
	}
	
	@Override
	protected void parseComplexDefinitionContent(HAPIdEntityInDomain entityId, JSONObject jsonObj, HAPContextParser parserContext) {
		HAPDefinitionEntityTestComplexScript entity = (HAPDefinitionEntityTestComplexScript)this.getEntity(entityId, parserContext);

		//script
		String scriptName = (String)jsonObj.opt(HAPDefinitionEntityTestComplexScript.ATTR_SCRIPTNAME);
		entity.setScriptName(scriptName);
		HAPResourceDefinition scriptResoureDef = this.getRuntimeEnvironment().getResourceDefinitionManager().getResourceDefinition(HAPFactoryResourceId.newInstance(HAPConstantShared.RUNTIME_RESOURCE_TYPE_SCRIPT, scriptName), parserContext.getGlobalDomain());
		HAPUtilityEntityDefinition.setEntitySimpleAttributeWithId(entity, HAPDefinitionEntityTestComplexScript.ATTR_SCRIPT, scriptResoureDef.getEntityId(), this.getRuntimeEnvironment().getDomainEntityManager());
		
		//parms
		JSONObject parms =  jsonObj.optJSONObject(HAPDefinitionEntityTestComplexScript.ATTR_PARM);
		if(parms!=null) {
			for(Object key : parms.keySet()) {
				String parmName = (String)key;
				entity.setParm(parmName, parms.opt(parmName));
			}
		}
	}

}
