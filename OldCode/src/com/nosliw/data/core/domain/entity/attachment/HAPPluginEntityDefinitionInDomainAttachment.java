package com.nosliw.data.core.domain.entity.attachment;

import org.json.JSONArray;
import org.json.JSONObject;

import com.nosliw.common.utils.HAPConstantShared;
import com.nosliw.data.core.domain.HAPContextParser;
import com.nosliw.data.core.domain.HAPExtraInfoEntityInDomainDefinition;
import com.nosliw.data.core.domain.HAPIdEntityInDomain;
import com.nosliw.data.core.domain.HAPInfoEntityInDomainDefinition;
import com.nosliw.data.core.domain.definition.HAPPluginEntityDefinitionInDomainImpSimple;
import com.nosliw.data.core.domain.definition.HAPUtilityParserEntityFormatJson;
import com.nosliw.data.core.runtime.HAPRuntimeEnvironment;

public class HAPPluginEntityDefinitionInDomainAttachment extends HAPPluginEntityDefinitionInDomainImpSimple{

	public HAPPluginEntityDefinitionInDomainAttachment(HAPRuntimeEnvironment runtimeEnv) {
		super(HAPConstantShared.RUNTIME_RESOURCE_TYPE_ATTACHMENT, HAPDefinitionEntityContainerAttachment.class, runtimeEnv);
	}

	@Override
	protected void parseDefinitionContentJson(HAPIdEntityInDomain entityId, Object jsonValue, HAPContextParser parserContext) {
		HAPDefinitionEntityContainerAttachment attachmentContainer = (HAPDefinitionEntityContainerAttachment)this.getEntity(entityId, parserContext);
		
		JSONObject jsonObj = (JSONObject)jsonValue;
		
		for(Object key : jsonObj.keySet()) {
			String valueType = (String)key;
			JSONArray byNameArray = jsonObj.getJSONArray(valueType);
			for(int i=0; i<byNameArray.length(); i++) {
				JSONObject attachmentJson = byNameArray.getJSONObject(i);
				HAPIdEntityInDomain attaEntityId = HAPUtilityParserEntityFormatJson.parseEntity(attachmentJson, valueType, parserContext, this.getRuntimeEnvironment().getDomainEntityDefinitionManager(), this.getRuntimeEnvironment().getResourceDefinitionManager());
				HAPInfoEntityInDomainDefinition entityInDomainInfo = parserContext.getCurrentDomain().getEntityInfoDefinition(attaEntityId);				
				HAPExtraInfoEntityInDomainDefinition entityInfo = entityInDomainInfo.getExtraInfo();
				HAPAttachment attachment = new HAPAttachmentImpEntity(valueType, attachmentJson, attaEntityId, entityInfo, entityInDomainInfo.getEntity());
				String name = entityInfo.getName();
				attachmentContainer.addAttachment(name, attachment);
			}
		}
	}

}
