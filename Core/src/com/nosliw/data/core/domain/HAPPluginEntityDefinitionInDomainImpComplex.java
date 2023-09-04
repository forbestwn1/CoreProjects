package com.nosliw.data.core.domain;

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
		HAPUtilityEntityDefinition.setupAttributeForComplexEntity(entityId, parserContext, getRuntimeEnvironment());
	}

	@Override
	protected void parseDefinitionContent(HAPIdEntityInDomain entityId, Object obj, HAPContextParser parserContext) {
		super.postParseDefinitionContent(entityId, parserContext);
		this.parseAttachmentAttribute(obj, entityId, parserContext);
		this.parseValueContextAttribute(obj, entityId, parserContext);
		this.parseComplexDefinitionContent(entityId, obj, parserContext);
	}

	@Override
	protected void postParseDefinitionContent(HAPIdEntityInDomain entityId, HAPContextParser parserContext) {
		super.postParseDefinitionContent(entityId, parserContext);
		HAPDefinitionEntityInDomainComplex complexEntity = this.getEntityComplex(entityId, parserContext);
		HAPDefinitionEntityValueContext valueContextEntity = complexEntity.getValueContextEntity(parserContext);
		valueContextEntity.discoverConstantScript(entityId, parserContext, this.getRuntimeEnvironment().getDataExpressionParser());
	}

	abstract protected void parseComplexDefinitionContent(HAPIdEntityInDomain entityId, Object obj, HAPContextParser parserContext);

	abstract protected void parseValueContextAttribute(Object obj, HAPIdEntityInDomain entityId, HAPContextParser parserContext);

	abstract protected void parseAttachmentAttribute(Object obj, HAPIdEntityInDomain entityId, HAPContextParser parserContext);

	protected HAPDefinitionEntityInDomainComplex getEntityComplex(HAPIdEntityInDomain entityId, HAPContextParser parserContext) {
		return (HAPDefinitionEntityInDomainComplex)this.getEntity(entityId, parserContext);
	}

}
