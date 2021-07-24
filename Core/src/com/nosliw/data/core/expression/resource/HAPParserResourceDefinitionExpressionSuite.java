package com.nosliw.data.core.expression.resource;

import org.json.JSONArray;
import org.json.JSONObject;

import com.nosliw.data.core.component.HAPResourceDefinitionContainer;
import com.nosliw.data.core.component.HAPParserComponent;
import com.nosliw.data.core.expression.HAPParserExpressionDefinition;
import com.nosliw.data.core.resource.HAPParserResourceDefinitionImp;
import com.nosliw.data.core.resource.HAPResourceDefinition;

public class HAPParserResourceDefinitionExpressionSuite extends HAPParserResourceDefinitionImp{

	@Override
	public HAPResourceDefinition parseJson(JSONObject jsonObj) {
		HAPResourceDefinitionExpressionSuite out = new HAPResourceDefinitionExpressionSuite();

		HAPParserComponent.parseComplextResourceDefinition(out, jsonObj);
		
		JSONArray expressionsArray = jsonObj.getJSONArray(HAPResourceDefinitionContainer.ELEMENT);
		for(int i=0; i<expressionsArray.length(); i++){
			JSONObject expressionObjJson = expressionsArray.getJSONObject(i);
			Object expressionObj = expressionObjJson.opt(HAPElementContainerResourceDefinitionEntityExpressionSuite.ELEMENT);
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

	
	private HAPElementContainerResourceDefinitionEntityExpressionSuite parseExpressionSuiteElement(JSONObject jsonObj) {
		HAPElementContainerResourceDefinitionEntityExpressionSuite out = new HAPElementContainerResourceDefinitionEntityExpressionSuite();
		HAPParserComponent.parseComponent(out, jsonObj);
		HAPParserExpressionDefinition.parseExpressionDefinitionList(out, jsonObj);
		return out;
	}

}
