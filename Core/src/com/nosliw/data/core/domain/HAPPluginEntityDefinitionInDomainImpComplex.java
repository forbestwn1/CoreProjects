package com.nosliw.data.core.domain;

import org.json.JSONObject;

import com.nosliw.common.utils.HAPConstantShared;
import com.nosliw.data.core.common.HAPWithValueContext;
import com.nosliw.data.core.component.HAPWithAttachment;
import com.nosliw.data.core.domain.entity.HAPDefinitionEntityInDomain;
import com.nosliw.data.core.domain.entity.HAPDefinitionEntityInDomainComplex;
import com.nosliw.data.core.domain.entity.valuestructure.HAPDefinitionEntityValueContext;
import com.nosliw.data.core.runtime.HAPRuntimeEnvironment;

public abstract class HAPPluginEntityDefinitionInDomainImpComplex extends HAPPluginEntityDefinitionInDomainImp{

	public HAPPluginEntityDefinitionInDomainImpComplex(String entityType, Class<? extends HAPDefinitionEntityInDomain> entityClass, HAPRuntimeEnvironment runtimeEnv) {
		super(entityType, entityClass, runtimeEnv);
	}

	@Override
	public boolean isComplexEntity() {    return true;   }
	
	@Override
	protected void postNewInstance(HAPIdEntityInDomain entityId, HAPContextParser parserContext) {
		super.postNewInstance(entityId, parserContext);
		this.setupAttributeForComplexEntity(entityId, parserContext);
	}
	
	protected void setupAttributeForComplexEntity(HAPIdEntityInDomain entityId, HAPContextParser parserContext) {
		HAPUtilityEntityDefinitionComplex.setupAttributeForComplexEntity(entityId, parserContext, getRuntimeEnvironment());
	}

	@Override
	protected void parseDefinitionContentJson(HAPIdEntityInDomain entityId, Object jsonValue, HAPContextParser parserContext) {
		JSONObject jsonObj = (JSONObject)jsonValue;
		this.parseSimpleEntityAttributeJson(jsonObj, entityId, HAPWithValueContext.VALUECONTEXT, HAPConstantShared.RUNTIME_RESOURCE_TYPE_VALUECONTEXT, null, parserContext);
		this.parseSimpleEntityAttributeJson(jsonObj, entityId, HAPWithAttachment.ATTACHMENT, HAPConstantShared.RUNTIME_RESOURCE_TYPE_ATTACHMENT, null, parserContext);
		
		this.parseComplexDefinitionContentJson(entityId, jsonObj, parserContext);
	}

	protected void parseComplexDefinitionContentJson(HAPIdEntityInDomain entityId, JSONObject jsonObj, HAPContextParser parserContext) {}

	@Override
	protected void postParseDefinitionContent(HAPIdEntityInDomain entityId, HAPContextParser parserContext) {
		super.postParseDefinitionContent(entityId, parserContext);
		HAPDefinitionEntityInDomainComplex complexEntity = this.getEntityComplex(entityId, parserContext);
		HAPDefinitionEntityValueContext valueContextEntity = complexEntity.getValueContextEntity(parserContext);
		if(valueContextEntity!=null)   valueContextEntity.discoverConstantScript(entityId, parserContext, this.getRuntimeEnvironment().getDataExpressionParser());
	}

	protected HAPDefinitionEntityInDomainComplex getEntityComplex(HAPIdEntityInDomain entityId, HAPContextParser parserContext) {
		return (HAPDefinitionEntityInDomainComplex)this.getEntity(entityId, parserContext);
	}
}
