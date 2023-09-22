package com.nosliw.data.core.domain;

import org.json.JSONObject;

import com.nosliw.common.utils.HAPConstantShared;
import com.nosliw.data.core.common.HAPWithValueContext;
import com.nosliw.data.core.component.HAPWithAttachment;
import com.nosliw.data.core.domain.entity.HAPDefinitionEntityInDomain;
import com.nosliw.data.core.runtime.HAPRuntimeEnvironment;

public abstract class HAPPluginEntityDefinitionInDomainImpComplexJson extends HAPPluginEntityDefinitionInDomainImpComplex{

	public HAPPluginEntityDefinitionInDomainImpComplexJson(String entityType, Class<? extends HAPDefinitionEntityInDomain> entityClass, HAPRuntimeEnvironment runtimeEnv) {
		super(entityType, entityClass, runtimeEnv);
	}

	@Override
	protected void parseComplexDefinitionContent(HAPIdEntityInDomain entityId, Object obj, HAPContextParser parserContext) {
		this.parseComplexDefinitionContentJson(entityId, this.convertToJsonObject(obj), parserContext);
	}

	abstract protected void parseComplexDefinitionContentJson(HAPIdEntityInDomain entityId, JSONObject jsonObj, HAPContextParser parserContext);

	@Override
	protected void parseValueContextAttribute(HAPIdEntityInDomain entityId, Object obj, HAPContextParser parserContext) {
		JSONObject jsonObj = this.convertToJsonObject(obj);
		this.parseSimpleEntityAttribute(jsonObj, entityId, HAPWithValueContext.VALUECONTEXT, HAPConstantShared.RUNTIME_RESOURCE_TYPE_VALUECONTEXT, null, parserContext);
	}

	@Override
	protected void parseAttachmentAttribute(HAPIdEntityInDomain entityId, Object obj, HAPContextParser parserContext) {
		JSONObject jsonObj = this.convertToJsonObject(obj);
		this.parseSimpleEntityAttribute(jsonObj, entityId, HAPWithAttachment.ATTACHMENT, HAPConstantShared.RUNTIME_RESOURCE_TYPE_ATTACHMENT, null, parserContext);
	}
}
