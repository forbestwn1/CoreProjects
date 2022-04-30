package com.nosliw.data.core.domain.entity.attachment;

import org.json.JSONObject;

import com.nosliw.data.core.domain.HAPContextParser;
import com.nosliw.data.core.domain.HAPIdEntityInDomain;
import com.nosliw.data.core.domain.HAPPluginEntityDefinitionInDomainImpSimple;
import com.nosliw.data.core.runtime.HAPRuntimeEnvironment;

public class HAPPluginEntityDefinitionInDomainAttachment extends HAPPluginEntityDefinitionInDomainImpSimple{

	public HAPPluginEntityDefinitionInDomainAttachment(HAPRuntimeEnvironment runtimeEnv) {
		super(HAPDefinitionEntityContainerAttachment.class, runtimeEnv);
	}

	@Override
	protected void parseDefinitionContent(HAPIdEntityInDomain entityId, Object obj, HAPContextParser parserContext) {
		HAPDefinitionEntityContainerAttachment attachmentContainer = (HAPDefinitionEntityContainerAttachment)this.getEntity(entityId, parserContext);
		HAPUtilityAttachment.parseDefinition((JSONObject)obj, attachmentContainer);
	}

}
