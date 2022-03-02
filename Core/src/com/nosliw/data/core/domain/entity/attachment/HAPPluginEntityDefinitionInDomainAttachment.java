package com.nosliw.data.core.domain.entity.attachment;

import org.json.JSONObject;

import com.nosliw.data.core.domain.HAPDomainDefinitionEntity;
import com.nosliw.data.core.domain.HAPIdEntityInDomain;
import com.nosliw.data.core.domain.HAPPluginEntityDefinitionInDomainImp;
import com.nosliw.data.core.runtime.HAPRuntimeEnvironment;

public class HAPPluginEntityDefinitionInDomainAttachment extends HAPPluginEntityDefinitionInDomainImp{

	public HAPPluginEntityDefinitionInDomainAttachment(HAPRuntimeEnvironment runtimeEnv) {
		super(HAPDefinitionEntityContainerAttachment.class, runtimeEnv);
	}

	@Override
	protected void parseDefinitionContent(HAPIdEntityInDomain entityId, Object obj,	HAPDomainDefinitionEntity definitionDomain) {
		HAPDefinitionEntityContainerAttachment attachmentContainer = (HAPDefinitionEntityContainerAttachment)this.getEntity(entityId, definitionDomain);
		HAPUtilityAttachment.parseDefinition((JSONObject)obj, attachmentContainer);
	}

}
