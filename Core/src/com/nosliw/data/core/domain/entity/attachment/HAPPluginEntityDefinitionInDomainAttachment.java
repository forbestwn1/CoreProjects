package com.nosliw.data.core.domain.entity.attachment;

import org.json.JSONObject;

import com.nosliw.data.core.domain.HAPDomainEntityDefinitionResource;
import com.nosliw.data.core.domain.HAPIdEntityInDomain;
import com.nosliw.data.core.domain.HAPPluginEntityDefinitionInDomainImpSimple;
import com.nosliw.data.core.runtime.HAPRuntimeEnvironment;

public class HAPPluginEntityDefinitionInDomainAttachment extends HAPPluginEntityDefinitionInDomainImpSimple{

	public HAPPluginEntityDefinitionInDomainAttachment(HAPRuntimeEnvironment runtimeEnv) {
		super(HAPDefinitionEntityContainerAttachment.class, runtimeEnv);
	}

	@Override
	protected void parseDefinitionContent(HAPIdEntityInDomain entityId, Object obj,	HAPDomainEntityDefinitionResource definitionDomain) {
		HAPDefinitionEntityContainerAttachment attachmentContainer = (HAPDefinitionEntityContainerAttachment)this.getEntity(entityId, definitionDomain);
		HAPUtilityAttachment.parseDefinition((JSONObject)obj, attachmentContainer);
	}

}
