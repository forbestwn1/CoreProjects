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
		super.parseDefinition(entityId, obj, parserContext);
		this.parseAttachmentAttribute(entityId, obj, parserContext);
		this.parseValueContextAttribute(entityId, obj, parserContext);
		this.parseComplexDefinitionContent(entityId, obj, parserContext);
	}

	abstract protected void parseValueContextAttribute(HAPIdEntityInDomain entityId, Object obj, HAPContextParser parserContext);

	abstract protected void parseAttachmentAttribute(HAPIdEntityInDomain entityId, Object obj, HAPContextParser parserContext);

	abstract protected void parseComplexDefinitionContent(HAPIdEntityInDomain entityId, Object obj, HAPContextParser parserContext);

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
