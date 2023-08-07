package com.nosliw.data.core.domain.entity.expression.script;

import org.json.JSONArray;
import org.json.JSONObject;

import com.nosliw.common.info.HAPUtilityEntityInfo;
import com.nosliw.common.utils.HAPConstantShared;
import com.nosliw.data.core.domain.HAPContextParser;
import com.nosliw.data.core.domain.HAPIdEntityInDomain;
import com.nosliw.data.core.domain.entity.expression.data.HAPDefinitionEntityExpressionDataGroup;
import com.nosliw.data.core.domain.entity.expression.data.HAPPluginEntityDefinitionInDomainImpComplexWithDataExpressionDataGroup;
import com.nosliw.data.core.domain.entity.expression.data.HAPUtilityDataExpressionDefinition;
import com.nosliw.data.core.runtime.HAPRuntimeEnvironment;

public class HAPPluginEntityDefinitionInDomainExpressionScriptGroup extends HAPPluginEntityDefinitionInDomainImpComplexWithDataExpressionDataGroup{

	public HAPPluginEntityDefinitionInDomainExpressionScriptGroup(HAPRuntimeEnvironment runtimeEnv) {
		super(HAPConstantShared.RUNTIME_RESOURCE_TYPE_SCRIPTEXPRESSIONGROUP, HAPDefinitionEntityExpressionScriptGroup.class, runtimeEnv);
	}

	@Override
	protected void parseComplexDefinitionContent(HAPIdEntityInDomain entityId, JSONObject jsonObj,	HAPContextParser parserContext) {
		
		HAPDefinitionEntityExpressionScriptGroup expressionGroupEntity = (HAPDefinitionEntityExpressionScriptGroup)this.getEntity(entityId, parserContext);
		HAPDefinitionEntityExpressionDataGroup dataExpressionGroup = (HAPDefinitionEntityExpressionDataGroup)this.getEntity(expressionGroupEntity.getDataExpressionGroup(), parserContext); 
		
		JSONArray eleArrayJson = jsonObj.optJSONArray(HAPDefinitionEntityExpressionScriptGroup.ELEMENT);
		if(eleArrayJson!=null) {
			for(int i=0; i<eleArrayJson.length(); i++) {
				JSONObject expressionJsonObj = eleArrayJson.getJSONObject(i);
				if(HAPUtilityEntityInfo.isEnabled(expressionJsonObj)) {
					String expressionType = (String)expressionJsonObj.opt(HAPDefinitionExpression.TYPE);
					HAPDefinitionExpression expressionDef = HAPUtilityExpressionDefinition.parseDefinitionExpression((String)expressionJsonObj.opt(HAPDefinitionExpression.EXPRESSION), expressionType, dataExpressionGroup, this.getRuntimeEnvironment().getDataExpressionParser());
					expressionDef.buildEntityInfoByJson(expressionJsonObj);
					expressionGroupEntity.addEntityElement(expressionDef);
				}
			}
		}
		else {
//			Object expressionObj = jsonObj.opt(HAPDefinitionExpression.EXPRESSION);
//			if(expressionObj!=null) {
//				//expression
//				expressionGroup.addEntityElement(HAPUtilityDataExpressionDefinition.parseExpressionDefinition(expressionObj, parserContext, expressionParser, this.getRuntimeEnvironment().getResourceDefinitionManager()));
//			}
		}
		
		HAPUtilityDataExpressionDefinition.processReferenceInExpression(expressionGroupEntity.getDataExpressionGroup(), parserContext, this.getRuntimeEnvironment().getResourceDefinitionManager());
	}

}
