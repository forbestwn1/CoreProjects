package com.nosliw.data.core.domain.entity.expression.data;

import org.json.JSONObject;

import com.nosliw.common.utils.HAPConstantShared;
import com.nosliw.data.core.domain.HAPContextParser;
import com.nosliw.data.core.domain.HAPIdEntityInDomain;
import com.nosliw.data.core.domain.HAPPluginEntityDefinitionInDomainImpComplexJson;
import com.nosliw.data.core.runtime.HAPRuntimeEnvironment;

public class HAPPluginEntityDefinitionInDomainExpressionDataSingle extends HAPPluginEntityDefinitionInDomainImpComplexJson{

	public HAPPluginEntityDefinitionInDomainExpressionDataSingle(HAPRuntimeEnvironment runtimeEnv) {
		super(HAPConstantShared.RUNTIME_RESOURCE_TYPE_DATAEXPRESSIONSINGLE, HAPDefinitionEntityExpressionDataSingle.class, runtimeEnv);
	}

	@Override
	protected void setupAttributeForComplexEntity(HAPIdEntityInDomain entityId, HAPContextParser parserContext) {	}

	@Override
	protected void parseComplexDefinitionContentJson(HAPIdEntityInDomain entityId, JSONObject jsonObj, HAPContextParser parserContext) {
		HAPDefinitionEntityExpressionDataSingle expressionEntity = (HAPDefinitionEntityExpressionDataSingle)this.getEntity(entityId, parserContext);
		String expression = jsonObj.getString(HAPDefinitionEntityExpressionDataSingle.ATTR_EXPRESSION);
		expressionEntity.setExpression(HAPUtilityDataExpressionDefinition.parseExpressionDefinition(
				expression, 
				this.getRuntimeEnvironment().getDataExpressionParser())); 
		HAPUtilityDataExpressionDefinition.processReferenceInExpression(entityId, parserContext, this.getRuntimeEnvironment().getResourceDefinitionManager());
	}
}
