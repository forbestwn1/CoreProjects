package com.nosliw.data.core.domain;

import org.json.JSONObject;

import com.nosliw.common.utils.HAPConstantShared;
import com.nosliw.data.core.common.HAPWithValueContext;
import com.nosliw.data.core.component.HAPWithAttachment;
import com.nosliw.data.core.domain.entity.HAPDefinitionEntityInDomain;
import com.nosliw.data.core.runtime.HAPRuntimeEnvironment;

public abstract class HAPPluginEntityDefinitionInDomainImpComplex extends HAPPluginEntityDefinitionInDomainImp{

	public HAPPluginEntityDefinitionInDomainImpComplex(String entityType, Class<? extends HAPDefinitionEntityInDomain> entityClass, HAPRuntimeEnvironment runtimeEnv) {
		super(entityClass, runtimeEnv);
	}

	public HAPPluginEntityDefinitionInDomainImpComplex(Class<? extends HAPDefinitionEntityInDomain> entityClass, HAPRuntimeEnvironment runtimeEnv) {
		super(entityClass, runtimeEnv);
	}

	@Override
	public boolean isComplexEntity() {    return true;   }
	
	@Override
	protected void parseDefinitionContent(HAPIdEntityInDomain entityId, Object obj, HAPContextParser parserContext) {
		JSONObject jsonObj = this.convertToJsonObject(obj);
		this.parseNormalSimpleEntityAttribute(jsonObj, entityId, HAPWithAttachment.ATTACHMENT, HAPConstantShared.RUNTIME_RESOURCE_TYPE_ATTACHMENT, null, parserContext);
		this.parseNormalSimpleEntityAttribute(jsonObj, entityId, HAPWithValueContext.VALUECONTEXT, HAPConstantShared.RUNTIME_RESOURCE_TYPE_VALUECONTEXT, null, parserContext);
		this.parseComplexDefinitionContent(entityId, jsonObj, parserContext);
	}
	
	abstract protected void parseComplexDefinitionContent(HAPIdEntityInDomain entityId, JSONObject jsonObj, HAPContextParser parserContext);

}
