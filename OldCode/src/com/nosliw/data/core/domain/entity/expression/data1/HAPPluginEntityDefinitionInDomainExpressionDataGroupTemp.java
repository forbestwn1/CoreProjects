package com.nosliw.data.core.domain.entity.expression.data1;

import com.nosliw.common.utils.HAPConstantShared;
import com.nosliw.data.core.domain.HAPContextParser;
import com.nosliw.data.core.domain.HAPIdEntityInDomain;
import com.nosliw.data.core.domain.definition.HAPPluginEntityDefinitionInDomainImpComplex;
import com.nosliw.data.core.domain.entity.HAPConfigureParentRelationComplex;
import com.nosliw.data.core.domain.entity.container.HAPUtilityEntityContainer;
import com.nosliw.data.core.runtime.HAPRuntimeEnvironment;

public class HAPPluginEntityDefinitionInDomainExpressionDataGroupTemp extends HAPPluginEntityDefinitionInDomainImpComplex{

	public HAPPluginEntityDefinitionInDomainExpressionDataGroupTemp(HAPRuntimeEnvironment runtimeEnv) {
		super(HAPConstantShared.RUNTIME_RESOURCE_TYPE_DATAEXPRESSIONGROUPTEMP, HAPDefinitionEntityExpressionDataGroup.class, runtimeEnv);
	}

	@Override
	protected void postNewInstance(HAPIdEntityInDomain entityId, HAPContextParser parserContext) {
		super.postNewInstance(entityId, parserContext);
	}
	
	@Override
	protected void setupAttributeForComplexEntity(HAPIdEntityInDomain entityId, HAPContextParser parserContext) {	
		//create reference container attribute
		HAPConfigureParentRelationComplex childRelationConfigure = new HAPConfigureParentRelationComplex();
		childRelationConfigure.getValueStructureRelationMode().getInheritProcessorConfigure().setMode(HAPConstantShared.INHERITMODE_DEFINITION);
		HAPUtilityEntityContainer.newComplexEntityContainerAttribute(entityId, HAPDefinitionEntityExpressionData.ATTR_REFERENCES, HAPConstantShared.RUNTIME_RESOURCE_TYPE_DATAEXPRESSIONSINGLE, childRelationConfigure, parserContext, getRuntimeEnvironment());
	}
}
