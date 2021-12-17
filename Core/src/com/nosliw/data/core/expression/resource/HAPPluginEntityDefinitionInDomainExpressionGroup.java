package com.nosliw.data.core.expression.resource;

import org.json.JSONArray;
import org.json.JSONObject;

import com.nosliw.data.core.complex.HAPUtilityComplexParser;
import com.nosliw.data.core.domain.HAPContextParser;
import com.nosliw.data.core.domain.HAPIdEntityInDomain;
import com.nosliw.data.core.domain.HAPPluginEntityDefinitionInDomainImp;
import com.nosliw.data.core.expression.HAPDefinitionExpression;
import com.nosliw.data.core.expression.HAPDefinitionExpressionGroup;
import com.nosliw.data.core.expression.HAPParserExpressionDefinition;

public class HAPPluginEntityDefinitionInDomainExpressionGroup extends HAPPluginEntityDefinitionInDomainImp{

	public HAPPluginEntityDefinitionInDomainExpressionGroup() {
		super(HAPDefinitionExpressionGroup.class);
	}
	
	@Override
	public HAPIdEntityInDomain parseDefinition(JSONObject jsonObj, HAPContextParser parserContext) {
		HAPDefinitionExpressionGroup entity = new HAPDefinitionExpressionGroup();
		
		//parse complex
		HAPUtilityComplexParser.parseContentInComplexEntity(entity, jsonObj, parserContext);

		//add to domain
		HAPIdEntityInDomain out = parserContext.getDefinitionDomain().addEntity(entity, parserContext.getLocalReferenceBase());
		
		return out;
	}

	@Override
	protected void parseDefinitionContent(HAPIdEntityInDomain entityId, JSONObject jsonObj, HAPContextParser parserContext) {
		HAPDefinitionExpressionGroup groupEntity = (HAPDefinitionExpressionGroup)this.getEntity(entityId, parserContext);
		//parse complex
		HAPUtilityComplexParser.parseContentInComplexEntity(groupEntity, jsonObj, parserContext);
		//parse expression items
		this.parseExpressionDefinitionList(groupEntity, jsonObj);
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
