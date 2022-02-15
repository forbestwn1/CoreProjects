package com.nosliw.data.core.domain.entity.expression;

import org.json.JSONArray;
import org.json.JSONObject;

import com.nosliw.data.core.domain.HAPDomainDefinitionEntity;
import com.nosliw.data.core.domain.HAPIdEntityInDomain;
import com.nosliw.data.core.domain.HAPPluginEntityDefinitionInDomainComplex;
import com.nosliw.data.core.runtime.HAPRuntimeEnvironment;

public class HAPPluginEntityDefinitionInDomainExpression extends HAPPluginEntityDefinitionInDomainComplex{

	public HAPPluginEntityDefinitionInDomainExpression(HAPRuntimeEnvironment runtimeEnv) {
		super(HAPDefinitionExpressionGroup.class, runtimeEnv);
	}

	@Override
	protected void parseComplexDefinitionContent(HAPIdEntityInDomain entityId, JSONObject jsonObj,
			HAPDomainDefinitionEntity definitionDomain) {
		HAPDefinitionExpressionGroup expressionGroupEntity = (HAPDefinitionExpressionGroup)definitionDomain.getComplexEntityInfo(entityId).getComplexEntity();
		
		//parse expression items
		this.parseExpressionDefinitionList(expressionGroupEntity, jsonObj);
	}
	
	private void parseExpressionDefinitionList(HAPDefinitionExpressionGroup expressionGroup, JSONObject jsonObj){
		JSONArray eleArrayJson = jsonObj.optJSONArray(HAPDefinitionExpressionGroup.ELEMENT);
		if(eleArrayJson!=null) {
			for(int i=0; i<eleArrayJson.length(); i++) {
				JSONObject expressionJsonObj = eleArrayJson.getJSONObject(i);
				Object expressionObj = expressionJsonObj.opt(HAPDefinitionExpression.EXPRESSION);
				if(expressionObj!=null) {
					//process
					expressionGroup.addEntityElement(HAPParserExpressionDefinition.parseExpressionDefinition(expressionJsonObj));
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
				expressionGroup.addEntityElement(HAPParserExpressionDefinition.parseExpressionDefinition(expressionObj));
			}
			else {
				//reference
//				out.addProcess(id, parseProcessReference(processObjJson));
			}
		}
	}


}
