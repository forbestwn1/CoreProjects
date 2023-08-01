package com.nosliw.data.core.domain.entity.expression.script;

import org.json.JSONArray;
import org.json.JSONObject;

import com.nosliw.common.utils.HAPConstantShared;
import com.nosliw.data.core.domain.HAPContextParser;
import com.nosliw.data.core.domain.HAPIdEntityInDomain;
import com.nosliw.data.core.domain.entity.expression.data.HAPPluginEntityDefinitionInDomainImpComplexWithDataExpressionDataGroup;
import com.nosliw.data.core.domain.entity.expression.data.HAPUtilityDataExpressionDefinition;
import com.nosliw.data.core.runtime.HAPRuntimeEnvironment;

public class HAPPluginEntityDefinitionInDomainExpressionScriptGroup extends HAPPluginEntityDefinitionInDomainImpComplexWithDataExpressionDataGroup{

	public HAPPluginEntityDefinitionInDomainExpressionScriptGroup(HAPRuntimeEnvironment runtimeEnv) {
		super(HAPConstantShared.RUNTIME_RESOURCE_TYPE_SCRIPTEXPRESSIONGROUP, HAPDefinitionEntityExpressionScriptGroup.class, runtimeEnv);
	}

	@Override
	protected void parseComplexDefinitionContent(HAPIdEntityInDomain entityId, JSONObject jsonObj,	HAPContextParser parserContext) {
		JSONArray eleArrayJson = jsonObj.optJSONArray(HAPDefinitionEntityExpressionScriptGroup.ELEMENT);
		if(eleArrayJson!=null) {
			for(int i=0; i<eleArrayJson.length(); i++) {
				JSONObject expressionJsonObj = eleArrayJson.getJSONObject(i);
				Object expressionObj = expressionJsonObj.opt(HAPDefinitionEntityExpressionScriptGroup.EXPRESSION);
				if(expressionObj!=null) {
					//expression
					expressionGroup.addEntityElement(HAPUtilityDataExpressionDefinition.parseExpressionDefinition(expressionJsonObj, parserContext, expressionParser, this.getRuntimeEnvironment().getResourceDefinitionManager()));
				}
			}
		}
		else {
			Object expressionObj = jsonObj.opt(HAPDefinitionEntityExpressionScriptGroup.EXPRESSION);
			if(expressionObj!=null) {
				//expression
				expressionGroup.addEntityElement(HAPUtilityDataExpressionDefinition.parseExpressionDefinition(expressionObj, parserContext, expressionParser, this.getRuntimeEnvironment().getResourceDefinitionManager()));
			}
		}
	}


}
