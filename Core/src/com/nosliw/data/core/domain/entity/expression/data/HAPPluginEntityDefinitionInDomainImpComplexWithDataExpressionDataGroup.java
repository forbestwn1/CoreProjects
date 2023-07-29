package com.nosliw.data.core.domain.entity.expression.data;

import com.nosliw.common.utils.HAPConstantShared;
import com.nosliw.data.core.domain.HAPContextParser;
import com.nosliw.data.core.domain.HAPIdEntityInDomain;
import com.nosliw.data.core.domain.HAPPluginEntityDefinitionInDomainImpComplex;
import com.nosliw.data.core.domain.entity.HAPDefinitionEntityInDomain;
import com.nosliw.data.core.runtime.HAPRuntimeEnvironment;

public abstract class HAPPluginEntityDefinitionInDomainImpComplexWithDataExpressionDataGroup extends HAPPluginEntityDefinitionInDomainImpComplex{

	public HAPPluginEntityDefinitionInDomainImpComplexWithDataExpressionDataGroup(String entityType,
			Class<? extends HAPDefinitionEntityInDomain> entityClass, HAPRuntimeEnvironment runtimeEnv) {
		super(entityType, entityClass, runtimeEnv);
	}

	@Override
	protected void postNewInstance(HAPIdEntityInDomain entityId, HAPContextParser parserContext) {
		HAPDefinitionEntityInDomain entity = parserContext.getGlobalDomain().getEntityInfoDefinition(entityId).getEntity();
		HAPIdEntityInDomain expressionGrouEntityId = this.getRuntimeEnvironment().getDomainEntityDefinitionManager().newDefinitionInstance(HAPConstantShared.RUNTIME_RESOURCE_TYPE_DATAEXPRESSIONGROUP, parserContext);
		entity.setAttributeValueComplex(HAPDefinitionEntityComplexWithDataExpressionGroup.ATTR_DATAEEXPRESSIONGROUP, expressionGrouEntityId);
	}

}
