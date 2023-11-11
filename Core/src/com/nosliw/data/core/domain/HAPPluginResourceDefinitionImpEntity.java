package com.nosliw.data.core.domain;

import org.json.JSONObject;

import com.nosliw.common.serialization.HAPSerializationFormat;
import com.nosliw.common.serialization.HAPUtilityJson;
import com.nosliw.data.core.resource.HAPPluginResourceDefinitionImp;
import com.nosliw.data.core.runtime.HAPRuntimeEnvironment;

public class HAPPluginResourceDefinitionImpEntity extends HAPPluginResourceDefinitionImp{

	public HAPPluginResourceDefinitionImpEntity(String resourceType, HAPRuntimeEnvironment runtimeEnv) {
		super(resourceType, runtimeEnv);
	}

	@Override
	protected HAPIdEntityInDomain parseEntity(Object content, HAPSerializationFormat format, HAPContextParser parserContext) {
		HAPIdEntityInDomain entityId = null;
		switch(format) {
		case JSON:
			JSONObject jsonObj = null;
			if(content instanceof JSONObject) jsonObj = (JSONObject)content;
			else if(content instanceof String)  jsonObj = new JSONObject(HAPUtilityJson.formatJson((String)content));

			Object entityObj = jsonObj.opt(HAPInfoEntityInDomainDefinition.ENTITY);
			if(entityObj==null)  entityObj = jsonObj;    //if no entity node, then using root
			entityId = this.getRuntimeEnvironment().getDomainEntityDefinitionManager().parseDefinition(this.getResourceType(), entityObj, format, parserContext);
		
			//entity info (name, description, ...)
			HAPInfoEntityInDomainDefinition entityInfo = parserContext.getCurrentDomain().getEntityInfoDefinition(entityId);
			HAPExtraInfoEntityInDomainDefinition entityInfoDef = entityInfo.getExtraInfo();
			JSONObject infoObj = jsonObj.optJSONObject(HAPInfoEntityInDomainDefinition.INFO);
			if(infoObj==null)   infoObj = jsonObj;
			entityInfoDef.buildObject(infoObj, HAPSerializationFormat.JSON);
		}
		
		return entityId;
	}
}
