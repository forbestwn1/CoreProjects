package com.nosliw.data.core.expression.resource;

import org.json.JSONArray;
import org.json.JSONObject;

import com.nosliw.data.core.component.HAPResourceDefinitionContainer;
import com.nosliw.data.core.component.HAPUtilityComponentParse;
import com.nosliw.data.core.expression.HAPParserExpressionDefinition;

public class HAPParserResourceExpressionDefinition {

	public static HAPResourceDefinitionExpressionSuite parseExpressionSuite(JSONObject expressionSuiteJson) {
		HAPResourceDefinitionExpressionSuite out = new HAPResourceDefinitionExpressionSuite();

		HAPUtilityComponentParse.parseComplextResourceDefinition(out, expressionSuiteJson);
		
		JSONArray expressionsArray = expressionSuiteJson.getJSONArray(HAPResourceDefinitionContainer.ELEMENT);
		for(int i=0; i<expressionsArray.length(); i++){
			JSONObject expressionObjJson = expressionsArray.getJSONObject(i);
			Object expressionObj = expressionObjJson.opt(HAPDefinitionResourceDefinitionExpressionSuiteElementEntity.ELEMENT);
			if(expressionObj!=null) {
				//expression entity
				out.addContainerElement(parseExpressionSuiteElement(expressionObjJson));
			}
			else {
				//reference
			}
		}
		return out;
	}

	public static HAPDefinitionResourceDefinitionExpressionSuiteElementEntity parseExpressionSuiteElement(JSONObject jsonObj) {
		HAPDefinitionResourceDefinitionExpressionSuiteElementEntity out = new HAPDefinitionResourceDefinitionExpressionSuiteElementEntity();
		HAPUtilityComponentParse.parseComponent(out, jsonObj);
		HAPParserExpressionDefinition.parseExpressionDefinitionList(out, jsonObj);
		return out;
	}
}
