package com.nosliw.data.core.domain.entity.expression.data1;

import org.json.JSONArray;
import org.json.JSONObject;

import com.nosliw.common.utils.HAPConstantShared;
import com.nosliw.core.application.common.dataexpression.definition.HAPParserDataExpression;
import com.nosliw.data.core.domain.HAPContextParser;
import com.nosliw.data.core.domain.HAPIdEntityInDomain;
import com.nosliw.data.core.domain.definition.HAPPluginEntityDefinitionInDomainImpComplex;
import com.nosliw.data.core.domain.entity.HAPConfigureParentRelationComplex;
import com.nosliw.data.core.domain.entity.container.HAPUtilityEntityContainer;
import com.nosliw.data.core.runtime.HAPRuntimeEnvironment;

public class HAPPluginEntityDefinitionInDomainExpressionDataGroup extends HAPPluginEntityDefinitionInDomainImpComplex{

	public HAPPluginEntityDefinitionInDomainExpressionDataGroup(HAPRuntimeEnvironment runtimeEnv) {
		super(HAPConstantShared.RUNTIME_RESOURCE_TYPE_DATAEXPRESSIONGROUP, HAPDefinitionEntityExpressionDataGroup.class, runtimeEnv);
	}

	@Override
	protected void postNewInstance(HAPIdEntityInDomain entityId, HAPContextParser parserContext) {
		super.postNewInstance(entityId, parserContext);
	}
	
	@Override
	protected void setupAttributeForComplexEntity(HAPIdEntityInDomain entityId, HAPContextParser parserContext) {	
		super.setupAttributeForComplexEntity(entityId, parserContext);
		//create reference container attribute
		HAPConfigureParentRelationComplex childRelationConfigure = new HAPConfigureParentRelationComplex();
		childRelationConfigure.getValueStructureRelationMode().getInheritProcessorConfigure().setMode(HAPConstantShared.INHERITMODE_DEFINITION);
		HAPUtilityEntityContainer.newComplexEntityContainerAttribute(entityId, HAPDefinitionEntityExpressionData.ATTR_REFERENCES, HAPConstantShared.RUNTIME_RESOURCE_TYPE_DATAEXPRESSIONSINGLE, childRelationConfigure, parserContext, getRuntimeEnvironment());
	}
	
	@Override
	protected void parseComplexDefinitionContentJson(HAPIdEntityInDomain entityId, JSONObject jsonObj, HAPContextParser parserContext) {
		HAPDefinitionEntityExpressionDataGroup expressionGroupEntity = (HAPDefinitionEntityExpressionDataGroup)this.getEntity(entityId, parserContext);

		//parse expression items
		this.parseExpressionDefinitionList(entityId, expressionGroupEntity, jsonObj, parserContext);
	}

	private void parseExpressionDefinitionList(HAPIdEntityInDomain expressionEntityId, HAPDefinitionEntityExpressionDataGroup expressionGroup, JSONObject jsonObj, HAPContextParser parserContext){
		HAPParserDataExpression expressionParser = this.getRuntimeEnvironment().getDataExpressionParser();
		JSONArray eleArrayJson = jsonObj.optJSONArray(HAPDefinitionEntityExpressionDataGroup.ELEMENT);
		if(eleArrayJson!=null) {
			for(int i=0; i<eleArrayJson.length(); i++) {
				JSONObject expressionJsonObj = eleArrayJson.getJSONObject(i);
				Object expressionObj = expressionJsonObj.opt(HAPDefinitionExpressionData.EXPRESSION);
				if(expressionObj!=null) {
					//expression
					expressionGroup.addEntityElement(HAPUtilityDataExpressionDefinition.parseExpressionDefinition(expressionJsonObj, expressionParser));
				}
			}
		}
		else {
			Object expressionObj = jsonObj.opt(HAPDefinitionExpressionData.EXPRESSION);
			if(expressionObj!=null) {
				//expression
				expressionGroup.addEntityElement(HAPUtilityDataExpressionDefinition.parseExpressionDefinition(expressionObj, expressionParser));
			}
		}
		HAPUtilityDataExpressionDefinition.processReferenceInExpression(expressionEntityId, parserContext, this.getRuntimeEnvironment().getResourceDefinitionManager());
	}
}
