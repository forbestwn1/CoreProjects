package com.nosliw.data.core.domain.entity.expression;

import org.json.JSONArray;
import org.json.JSONObject;

import com.nosliw.data.core.domain.HAPDomainEntityDefinitionLocal;
import com.nosliw.data.core.domain.HAPIdEntityInDomain;
import com.nosliw.data.core.domain.HAPPluginEntityDefinitionInDomainImpComplex;
import com.nosliw.data.core.runtime.HAPRuntimeEnvironment;

public class HAPPluginEntityDefinitionInDomainExpression extends HAPPluginEntityDefinitionInDomainImpComplex{

	public HAPPluginEntityDefinitionInDomainExpression(HAPRuntimeEnvironment runtimeEnv) {
		super(HAPDefinitionEntityExpressionGroup.class, runtimeEnv);
	}

	@Override
	protected void parseComplexDefinitionContent(HAPIdEntityInDomain entityId, JSONObject jsonObj,
			HAPDomainEntityDefinitionLocal definitionDomain) {
		HAPDefinitionEntityExpressionGroup expressionGroupEntity = (HAPDefinitionEntityExpressionGroup)definitionDomain.getEntityInfoDefinition(entityId).getEntity();
		
		//parse expression items
		this.parseExpressionDefinitionList(expressionGroupEntity, jsonObj);
	}
	
	private void parseExpressionDefinitionList(HAPDefinitionEntityExpressionGroup expressionGroup, JSONObject jsonObj){
		JSONArray eleArrayJson = jsonObj.optJSONArray(HAPDefinitionEntityExpressionGroup.ELEMENT);
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
