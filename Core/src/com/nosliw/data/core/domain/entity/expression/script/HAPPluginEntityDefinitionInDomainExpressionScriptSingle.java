package com.nosliw.data.core.domain.entity.expression.script;

import org.json.JSONObject;

import com.nosliw.common.utils.HAPConstantShared;
import com.nosliw.data.core.domain.HAPContextParser;
import com.nosliw.data.core.domain.HAPIdEntityInDomain;
import com.nosliw.data.core.domain.HAPPluginEntityDefinitionInDomainImpComplex;
import com.nosliw.data.core.domain.entity.expression.data.HAPDefinitionEntityExpressionDataGroup;
import com.nosliw.data.core.domain.entity.expression.data.HAPDefinitionEntityExpressionDataSingle;
import com.nosliw.data.core.runtime.HAPRuntimeEnvironment;

public class HAPPluginEntityDefinitionInDomainExpressionScriptSingle extends HAPPluginEntityDefinitionInDomainImpComplex{

	public HAPPluginEntityDefinitionInDomainExpressionScriptSingle(HAPRuntimeEnvironment runtimeEnv) {
		super(HAPConstantShared.RUNTIME_RESOURCE_TYPE_SCRIPTEXPRESSIONSINGLE, HAPDefinitionEntityExpressionScriptSingle.class, runtimeEnv);
	}

	@Override
	protected void parseComplexDefinitionContent(HAPIdEntityInDomain entityId, JSONObject jsonObj, HAPContextParser parserContext) {
		HAPDefinitionEntityExpressionScriptSingle expressionSingleEntity = (HAPDefinitionEntityExpressionScriptSingle)this.getEntity(entityId, parserContext);
		HAPDefinitionEntityExpressionDataGroup dataExpressionGroup = (HAPDefinitionEntityExpressionDataGroup)this.getEntity(expressionSingleEntity.getDataExpressionGroup(), parserContext); 

		String expressionType = (String)jsonObj.opt(HAPDefinitionEntityExpressionDataSingle.ATTR_TYPE);
		HAPDefinitionExpression expressionDef = HAPUtilityScriptExpressionDefinition.parseDefinitionExpression(jsonObj.getString(HAPDefinitionEntityExpressionDataSingle.ATTR_EXPRESSION), expressionType, dataExpressionGroup, this.getRuntimeEnvironment().getDataExpressionParser());
		expressionSingleEntity.setExpression(expressionDef);
	}
}
