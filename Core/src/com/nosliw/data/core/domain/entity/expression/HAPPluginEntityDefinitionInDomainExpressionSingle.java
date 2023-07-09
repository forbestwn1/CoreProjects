package com.nosliw.data.core.domain.entity.expression;

import org.json.JSONObject;

import com.nosliw.common.utils.HAPConstantShared;
import com.nosliw.data.core.domain.HAPContextParser;
import com.nosliw.data.core.domain.HAPIdEntityInDomain;
import com.nosliw.data.core.domain.HAPPluginEntityDefinitionInDomainImpComplex;
import com.nosliw.data.core.runtime.HAPRuntimeEnvironment;

public class HAPPluginEntityDefinitionInDomainExpressionSingle extends HAPPluginEntityDefinitionInDomainImpComplex{

	public HAPPluginEntityDefinitionInDomainExpressionSingle(HAPRuntimeEnvironment runtimeEnv) {
		super(HAPConstantShared.RUNTIME_RESOURCE_TYPE_DATAEXPRESSIONSINGLE, HAPDefinitionEntityExpressionSingle.class, runtimeEnv);
	}

	@Override
	protected void parseComplexDefinitionContent(HAPIdEntityInDomain entityId, JSONObject jsonObj, HAPContextParser parserContext) {
		HAPDefinitionEntityExpressionSingle expressionEntity = (HAPDefinitionEntityExpressionSingle)this.getEntity(entityId, parserContext);
		String expression = jsonObj.getString(HAPDefinitionEntityExpressionSingle.ATTR_EXPRESSION);
		expressionEntity.setExpression(HAPParserExpressionDefinition.parseExpressionDefinition(
				expression, 
				parserContext, 
				this.getRuntimeEnvironment().getExpressionManager().getExpressionParser(), 
				this.getRuntimeEnvironment().getResourceDefinitionManager()));
		HAPParserExpressionDefinition.processReferenceInExpression(expressionEntity, parserContext, this.getRuntimeEnvironment().getResourceDefinitionManager());
	}
}
