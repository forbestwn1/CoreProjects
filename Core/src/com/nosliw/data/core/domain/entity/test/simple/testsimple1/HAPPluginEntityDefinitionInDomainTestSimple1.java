package com.nosliw.data.core.domain.entity.test.simple.testsimple1;

import org.json.JSONObject;

import com.nosliw.common.utils.HAPConstantShared;
import com.nosliw.data.core.domain.HAPContextParser;
import com.nosliw.data.core.domain.HAPIdEntityInDomain;
import com.nosliw.data.core.domain.definition.HAPPluginEntityDefinitionInDomainImpSimple;
import com.nosliw.data.core.domain.definition.HAPUtilityEntityDefinition;
import com.nosliw.data.core.resource.HAPFactoryResourceId;
import com.nosliw.data.core.resource.HAPResourceDefinition;
import com.nosliw.data.core.runtime.HAPRuntimeEnvironment;

public class HAPPluginEntityDefinitionInDomainTestSimple1 extends HAPPluginEntityDefinitionInDomainImpSimple{

	public HAPPluginEntityDefinitionInDomainTestSimple1(HAPRuntimeEnvironment runtimeEnv) {
		super(HAPConstantShared.RUNTIME_RESOURCE_TYPE_TEST_SIMPLE1, HAPDefinitionEntityTestSimple1.class, runtimeEnv);
	}
	
	@Override
	protected void parseDefinitionContentJson(HAPIdEntityInDomain entityId, Object jsonValue, HAPContextParser parserContext) {
		HAPDefinitionEntityTestSimple1 entity = (HAPDefinitionEntityTestSimple1)this.getEntity(entityId, parserContext);

		JSONObject jsonObj = (JSONObject)jsonValue;
		
		//script
		String scriptName = (String)jsonObj.opt(HAPDefinitionEntityTestSimple1.ATTR_SCRIPTNAME);
		entity.setScriptName(scriptName);
		HAPResourceDefinition scriptResoureDef = this.getRuntimeEnvironment().getResourceDefinitionManager().getResourceDefinition(HAPFactoryResourceId.newInstance(HAPConstantShared.RUNTIME_RESOURCE_TYPE_SCRIPT, scriptName), parserContext.getGlobalDomain());
		HAPUtilityEntityDefinition.setEntitySimpleAttributeWithId(entity, HAPDefinitionEntityTestSimple1.ATTR_SCRIPT, scriptResoureDef.getEntityId(), this.getRuntimeEnvironment().getDomainEntityDefinitionManager());
		
		//parms
		JSONObject parms =  jsonObj.optJSONObject(HAPDefinitionEntityTestSimple1.ATTR_PARM);
		if(parms!=null) {
			for(Object key : parms.keySet()) {
				String parmName = (String)key;
				entity.setParm(parmName, parms.opt(parmName));
			}
		}
	}
}
