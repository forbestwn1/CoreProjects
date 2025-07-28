package com.nosliw.data.core.domain;

import com.nosliw.common.utils.HAPConstantShared;
import com.nosliw.core.application.division.manual.HAPManualBrickComplex;
import com.nosliw.data.core.domain.definition.HAPUtilityEntityDefinition;
import com.nosliw.data.core.domain.entity.HAPExecutableEntityComplex;
import com.nosliw.data.core.domain.entity.expression.script.HAPDefinitionExpression;
import com.nosliw.data.core.runtime.HAPRuntimeEnvironment;

public class HAPUtilityEntityDefinitionComplex {

	public static void setupAttributeForComplexEntity(HAPIdEntityInDomain entityId, HAPContextParser parserContext, HAPRuntimeEnvironment runtimeEnv) {
		HAPUtilityEntityDefinition.newTransparentAttribute(entityId, HAPConstantShared.RUNTIME_RESOURCE_TYPE_DATAEXPRESSIONGROUPTEMP, HAPExecutableEntityComplex.DATAEEXPRESSIONGROUP, parserContext, runtimeEnv);
		HAPUtilityEntityDefinition.newTransparentAttribute(entityId, HAPConstantShared.RUNTIME_RESOURCE_TYPE_SCRIPTEXPRESSIONGROUPTEMP, HAPExecutableEntityComplex.SCRIPTEEXPRESSIONGROUP, parserContext, runtimeEnv);
		HAPUtilityEntityDefinition.newTransparentAttribute(entityId, HAPConstantShared.RUNTIME_RESOURCE_TYPE_SCRIPTEXPRESSIONGROUPTEMP, HAPExecutableEntityComplex.PLAINSCRIPTEEXPRESSIONGROUP, parserContext, runtimeEnv);
	}

	public static String addPlainScriptExpressionToComplexEntity(HAPDefinitionExpression expression, HAPIdEntityInDomain complexEntityId, HAPContextParser parserContext) {
		HAPManualBrickComplex complexEntity = (HAPManualBrickComplex)parserContext.getGlobalDomain().getEntityInfoDefinition(complexEntityId).getEntity();
		return complexEntity.getPlainScriptExpressionGroupEntity(parserContext).addExpression(expression);
	}
	
}
