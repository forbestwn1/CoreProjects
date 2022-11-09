package com.nosliw.data.core.domain;

import org.json.JSONObject;

import com.nosliw.common.serialization.HAPUtilityJson;
import com.nosliw.data.core.resource.HAPPluginResourceDefinitionImp;
import com.nosliw.data.core.runtime.HAPRuntimeEnvironment;

public class HAPPluginResourceDefinitionImpEntity extends HAPPluginResourceDefinitionImp{

	public HAPPluginResourceDefinitionImpEntity(String resourceType, HAPRuntimeEnvironment runtimeEnv) {
		super(resourceType, runtimeEnv);
	}

	@Override
	protected HAPIdEntityInDomain parseEntity(Object content, HAPContextParser parserContext) {
		JSONObject jsonObj = null;
		if(content instanceof JSONObject) jsonObj = (JSONObject)content;
		else if(content instanceof String)  jsonObj = new JSONObject(HAPUtilityJson.formatJson((String)content));
		HAPIdEntityInDomain entityId = HAPUtilityParserEntity.parseEntity(jsonObj, this.getResourceType(), parserContext, this.getRuntimeEnvironment().getDomainEntityManager(), this.getRuntimeEnvironment().getResourceDefinitionManager()); 
		return entityId;
	}

}
