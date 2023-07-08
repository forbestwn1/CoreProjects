package com.nosliw.data.core.domain.entity.expression;

import org.json.JSONArray;
import org.json.JSONObject;

import com.nosliw.common.utils.HAPConstantShared;
import com.nosliw.data.core.domain.HAPContextParser;
import com.nosliw.data.core.domain.HAPIdEntityInDomain;
import com.nosliw.data.core.domain.HAPPluginEntityDefinitionInDomainImpComplex;
import com.nosliw.data.core.runtime.HAPRuntimeEnvironment;

public class HAPPluginEntityDefinitionInDomainExpressionGroup extends HAPPluginEntityDefinitionInDomainImpComplex{

	public HAPPluginEntityDefinitionInDomainExpressionGroup(HAPRuntimeEnvironment runtimeEnv) {
		super(HAPConstantShared.RUNTIME_RESOURCE_TYPE_DATAEXPRESSIONGROUP, HAPDefinitionEntityExpressionGroup.class, runtimeEnv);
	}

	@Override
	protected void parseComplexDefinitionContent(HAPIdEntityInDomain entityId, JSONObject jsonObj, HAPContextParser parserContext) {
		HAPDefinitionEntityExpressionGroup expressionGroupEntity = (HAPDefinitionEntityExpressionGroup)this.getEntity(entityId, parserContext);
		
		//parse expression items
		this.parseExpressionDefinitionList(expressionGroupEntity, jsonObj, parserContext);
	}

	private void parseExpressionDefinitionList(HAPDefinitionEntityExpressionGroup expressionGroup, JSONObject jsonObj, HAPContextParser parserContext){
		HAPParserExpression expressionParser = this.getRuntimeEnvironment().getExpressionManager().getExpressionParser();
		JSONArray eleArrayJson = jsonObj.optJSONArray(HAPDefinitionEntityExpressionGroup.ELEMENT);
		if(eleArrayJson!=null) {
			for(int i=0; i<eleArrayJson.length(); i++) {
				JSONObject expressionJsonObj = eleArrayJson.getJSONObject(i);
				Object expressionObj = expressionJsonObj.opt(HAPDefinitionExpression.EXPRESSION);
				if(expressionObj!=null) {
					//process
					expressionGroup.addEntityElement(HAPParserExpressionDefinition.parseExpressionDefinition(expressionJsonObj, parserContext, expressionParser, this.getRuntimeEnvironment().getResourceDefinitionManager()));
				}
				else {
					//reference
//					out.addProcess(id, parseProcessReference(processObjJson));
				}
			}
		}
		else {
			Object expressionObj = jsonObj.opt(HAPDefinitionExpression.EXPRESSION);
			if(expressionObj!=null) {
				//process
				expressionGroup.addEntityElement(HAPParserExpressionDefinition.parseExpressionDefinition(expressionObj, parserContext, expressionParser, this.getRuntimeEnvironment().getResourceDefinitionManager()));
			}
			else {
				//reference
//				out.addProcess(id, parseProcessReference(processObjJson));
			}
		}
		HAPParserExpressionDefinition.processReferenceInExpression(expressionGroup, parserContext, this.getRuntimeEnvironment().getResourceDefinitionManager());
	}
}
