package com.nosliw.data.core.domain.entity.expression.script;

import org.json.JSONArray;
import org.json.JSONObject;

import com.nosliw.common.info.HAPUtilityEntityInfo;
import com.nosliw.common.utils.HAPConstantShared;
import com.nosliw.data.core.domain.HAPContextParser;
import com.nosliw.data.core.domain.HAPIdEntityInDomain;
import com.nosliw.data.core.domain.definition.HAPPluginEntityDefinitionInDomainImpComplex;
import com.nosliw.data.core.domain.entity.HAPConfigureParentRelationComplex;
import com.nosliw.data.core.domain.entity.container.HAPUtilityEntityContainer;
import com.nosliw.data.core.domain.entity.expression.data1.HAPDefinitionEntityExpressionDataGroup;
import com.nosliw.data.core.domain.entity.expression.data1.HAPUtilityDataExpressionDefinition;
import com.nosliw.data.core.runtime.HAPRuntimeEnvironment;

public class HAPPluginEntityDefinitionInDomainExpressionScriptGroup extends HAPPluginEntityDefinitionInDomainImpComplex{

	public HAPPluginEntityDefinitionInDomainExpressionScriptGroup(HAPRuntimeEnvironment runtimeEnv) {
		super(HAPConstantShared.RUNTIME_RESOURCE_TYPE_SCRIPTEXPRESSIONGROUP, HAPDefinitionEntityExpressionScriptGroup.class, runtimeEnv);
	}

	@Override
	protected void setupAttributeForComplexEntity(HAPIdEntityInDomain entityId, HAPContextParser parserContext) {
		super.setupAttributeForComplexEntity(entityId, parserContext);
		//create reference container attribute
		HAPConfigureParentRelationComplex childRelationConfigure = new HAPConfigureParentRelationComplex();
		childRelationConfigure.getValueStructureRelationMode().getInheritProcessorConfigure().setMode(HAPConstantShared.INHERITMODE_DEFINITION);
		HAPUtilityEntityContainer.newComplexEntityContainerAttribute(entityId, HAPDefinitionEntityExpressionScript.ATTR_REFERENCES, HAPConstantShared.RUNTIME_RESOURCE_TYPE_DATAEXPRESSIONSINGLE, childRelationConfigure, parserContext, getRuntimeEnvironment());
	}

	
	@Override
	protected void parseComplexDefinitionContentJson(HAPIdEntityInDomain entityId, JSONObject jsonObj,	HAPContextParser parserContext) {
		
		HAPDefinitionEntityExpressionScriptGroup expressionGroupEntity = (HAPDefinitionEntityExpressionScriptGroup)this.getEntity(entityId, parserContext);
		HAPDefinitionEntityExpressionDataGroup dataExpressionGroup = (HAPDefinitionEntityExpressionDataGroup)this.getEntity(expressionGroupEntity.getDataExpressions(), parserContext); 
		
		JSONArray eleArrayJson = jsonObj.optJSONArray(HAPDefinitionEntityExpressionScriptGroup.ELEMENT);
		if(eleArrayJson!=null) {
			for(int i=0; i<eleArrayJson.length(); i++) {
				JSONObject expressionJsonObj = eleArrayJson.getJSONObject(i);
				if(HAPUtilityEntityInfo.isEnabled(expressionJsonObj)) {
					String expressionType = (String)expressionJsonObj.opt(HAPDefinitionExpression.TYPE);
					HAPDefinitionExpression expressionDef = HAPUtilityScriptExpressionDefinition.parseDefinitionExpression((String)expressionJsonObj.opt(HAPDefinitionExpression.EXPRESSION), expressionType, dataExpressionGroup, this.getRuntimeEnvironment().getDataExpressionParser());
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
		
		HAPUtilityDataExpressionDefinition.processReferenceInExpression(expressionGroupEntity.getDataExpressions(), parserContext, this.getRuntimeEnvironment().getResourceDefinitionManager());
	}

}
