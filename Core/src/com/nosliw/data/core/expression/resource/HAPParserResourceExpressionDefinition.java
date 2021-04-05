package com.nosliw.data.core.expression.resource;

import org.json.JSONArray;
import org.json.JSONObject;

import com.nosliw.data.core.component.HAPResourceDefinitionContainer;
import com.nosliw.data.core.component.HAPUtilityComponentParse;
import com.nosliw.data.core.expression.HAPParserExpressionDefinition;
import com.nosliw.data.core.resource.HAPParserResourceImp;
import com.nosliw.data.core.resource.HAPResourceDefinition;

public class HAPParserResourceExpressionDefinition extends HAPParserResourceImp{

	@Override
	public HAPResourceDefinition parseJson(JSONObject jsonObj) {
		HAPResourceDefinitionExpressionSuite out = new HAPResourceDefinitionExpressionSuite();

		HAPUtilityComponentParse.parseComplextResourceDefinition(out, jsonObj);
		
		JSONArray expressionsArray = jsonObj.getJSONArray(HAPResourceDefinitionContainer.ELEMENT);
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

	
	private HAPDefinitionResourceDefinitionExpressionSuiteElementEntity parseExpressionSuiteElement(JSONObject jsonObj) {
		HAPDefinitionResourceDefinitionExpressionSuiteElementEntity out = new HAPDefinitionResourceDefinitionExpressionSuiteElementEntity();
		HAPUtilityComponentParse.parseComponent(out, jsonObj);
		HAPParserExpressionDefinition.parseExpressionDefinitionList(out, jsonObj);
		return out;
	}

}
