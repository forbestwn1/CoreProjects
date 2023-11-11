package com.nosliw.data.core.domain.entity.expression.script;

import com.nosliw.common.utils.HAPConstantShared;
import com.nosliw.data.core.domain.HAPContextParser;
import com.nosliw.data.core.domain.HAPIdEntityInDomain;
import com.nosliw.data.core.domain.HAPPluginEntityDefinitionInDomainImpComplex;
import com.nosliw.data.core.domain.HAPUtilityEntityDefinition;
import com.nosliw.data.core.domain.entity.HAPConfigureParentRelationComplex;
import com.nosliw.data.core.domain.entity.HAPExecutableEntityComplex;
import com.nosliw.data.core.domain.entity.container.HAPUtilityEntityContainer;
import com.nosliw.data.core.runtime.HAPRuntimeEnvironment;

public class HAPPluginEntityDefinitionInDomainExpressionScriptGroupTemp extends HAPPluginEntityDefinitionInDomainImpComplex{

	public HAPPluginEntityDefinitionInDomainExpressionScriptGroupTemp(HAPRuntimeEnvironment runtimeEnv) {
		super(HAPConstantShared.RUNTIME_RESOURCE_TYPE_SCRIPTEXPRESSIONGROUPTEMP, HAPDefinitionEntityExpressionScriptGroup.class, runtimeEnv);
	}

	@Override
	protected void setupAttributeForComplexEntity(HAPIdEntityInDomain entityId, HAPContextParser parserContext) {
		HAPUtilityEntityDefinition.newTransparentAttribute(entityId, HAPConstantShared.RUNTIME_RESOURCE_TYPE_DATAEXPRESSIONGROUPTEMP, HAPExecutableEntityComplex.DATAEEXPRESSIONGROUP, parserContext, this.getRuntimeEnvironment());
		//create reference container attribute
		HAPConfigureParentRelationComplex childRelationConfigure = new HAPConfigureParentRelationComplex();
		childRelationConfigure.getValueStructureRelationMode().getInheritProcessorConfigure().setMode(HAPConstantShared.INHERITMODE_DEFINITION);
		HAPUtilityEntityContainer.newComplexEntityContainerAttribute(entityId, HAPDefinitionEntityExpressionScript.ATTR_REFERENCES, HAPConstantShared.RUNTIME_RESOURCE_TYPE_DATAEXPRESSIONSINGLE, childRelationConfigure, parserContext, getRuntimeEnvironment());
	}
}
