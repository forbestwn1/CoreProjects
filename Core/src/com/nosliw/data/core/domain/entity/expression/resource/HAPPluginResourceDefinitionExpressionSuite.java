package com.nosliw.data.core.domain.entity.expression.resource;

import org.json.JSONArray;
import org.json.JSONObject;

import com.nosliw.common.utils.HAPConstantShared;
import com.nosliw.data.core.complex.HAPDefinitionEntityContainer;
import com.nosliw.data.core.component.HAPPathLocationBase;
import com.nosliw.data.core.component.HAPParserEntityComponent;
import com.nosliw.data.core.domain.HAPDomainDefinitionEntity;
import com.nosliw.data.core.domain.HAPIdEntityInDomain;
import com.nosliw.data.core.domain.HAPInfoComplexEntityDefinition;
import com.nosliw.data.core.domain.entity.expression.HAPParserExpressionDefinition;
import com.nosliw.data.core.resource.HAPPluginResourceDefinitionImpDefault;
import com.nosliw.data.core.task.HAPManagerTask;

public class HAPPluginResourceDefinitionExpressionSuite extends HAPPluginResourceDefinitionImpDefault{

	private HAPManagerTask m_taskMan;

	public HAPPluginResourceDefinitionExpressionSuite() {
		super(HAPConstantShared.RUNTIME_RESOURCE_TYPE_DATAEXPRESSIONSUITE);
	}
	
	@Override
	public HAPIdEntityInDomain parseJson(JSONObject jsonObj, HAPDomainDefinitionEntity entityDomain, HAPPathLocationBase localRefBase) {
		
		
		
		HAPResourceEntityExpressionSuite suiteEntity = new HAPResourceEntityExpressionSuite();

		HAPParserEntityComponent.parseComplextResourceDefinition(suiteEntity, jsonObj);

		JSONArray expressionsArray = jsonObj.getJSONArray(HAPDefinitionEntityContainer.ELEMENT);
		for(int i=0; i<expressionsArray.length(); i++){
			JSONObject expressionObjJson = expressionsArray.getJSONObject(i);
			Object expressionObj = expressionObjJson.opt(HAPElementContainerResourceDefinitionEntityExpressionSuite.ELEMENT);
			if(expressionObj!=null) {
				//expression entity
				suiteEntity.addContainerElement(parseExpressionSuiteElement(expressionObjJson));
			}
			else {
				//reference
			}
		}
		HAPIdEntityInDomain entityId = entityDomain.addComplexEntity(new HAPInfoComplexEntityDefinition(suiteEntity, null));
		
		return entityId;
	}

	private HAPElementContainerResourceDefinitionEntityExpressionSuite parseExpressionSuiteElement(JSONObject jsonObj) {
		HAPElementContainerResourceDefinitionEntityExpressionSuite out = new HAPElementContainerResourceDefinitionEntityExpressionSuite();
		HAPParserEntityComponent.parseComponentEntity(out, jsonObj, this.m_taskMan);
		HAPParserExpressionDefinition.parseExpressionDefinitionList(out, jsonObj);
		return out;
	}
}
