package com.nosliw.data.core.domain.entity.expression.resource;

import org.json.JSONArray;
import org.json.JSONObject;

import com.nosliw.data.core.component.HAPParserEntityComponent;
import com.nosliw.data.core.domain.complexentity.HAPDefinitionEntityContainer;
import com.nosliw.data.core.domain.entity.expression.data.HAPParserExpressionDefinition;
import com.nosliw.data.core.resource.HAPEntityResourceDefinition;
import com.nosliw.data.core.resource.HAPParserResourceEntityImp;
import com.nosliw.data.core.task.HAPManagerTask;

public class HAPParserResourceDefinitionExpressionSuite extends HAPParserResourceEntityImp{

	private HAPManagerTask m_taskMan;
	
	public HAPParserResourceDefinitionExpressionSuite(HAPManagerTask taskMan) {
		this.m_taskMan = taskMan;
	}
	 
	@Override
	public HAPEntityResourceDefinition parseJson(JSONObject jsonObj) {
		HAPResourceEntityExpressionSuite out = new HAPResourceEntityExpressionSuite();

		HAPParserEntityComponent.parseComplextResourceDefinition(out, jsonObj);
		
		JSONArray expressionsArray = jsonObj.getJSONArray(HAPDefinitionEntityContainer.ELEMENT);
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
		HAPParserEntityComponent.parseComponentEntity(out, jsonObj, this.m_taskMan);
		HAPParserExpressionDefinition.parseExpressionDefinitionList(out, jsonObj);
		return out;
	}

}
