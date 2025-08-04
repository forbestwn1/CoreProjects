package com.nosliw.core.application.division.manual.core.definition;

import org.json.JSONObject;

import com.nosliw.core.application.HAPIdBrickType;
import com.nosliw.core.application.division.manual.core.a.HAPManualEnumBrickType;
import com.nosliw.core.xxx.application1.HAPWithValueContext;

public class HAPManualDefinitionPluginParserBrickImpComplex extends HAPManualDefinitionPluginParserBrickImp{

	public HAPManualDefinitionPluginParserBrickImpComplex(HAPIdBrickType brickTypeId, Class<? extends HAPManualDefinitionBrick> brickClass) {
		super(brickTypeId, brickClass);
	}


	@Override
	protected void parseDefinitionContentJson(HAPManualDefinitionBrick brickDefinition, Object jsonValue, HAPManualDefinitionContextParse parseContext) {
		JSONObject jsonObj = (JSONObject)jsonValue;
		
		parseBrickAttributeJson(brickDefinition, jsonObj, HAPWithValueContext.VALUECONTEXT, HAPManualEnumBrickType.VALUECONTEXT_100, null, parseContext);	
		
//		this.parseSimpleEntityAttributeJson(jsonObj, entityId, HAPWithAttachment.ATTACHMENT, HAPConstantShared.RUNTIME_RESOURCE_TYPE_ATTACHMENT, null, parserContext);
//		
		this.parseComplexDefinitionContentJson(brickDefinition, jsonObj, parseContext);
	}

	protected void parseComplexDefinitionContentJson(HAPManualDefinitionBrick brickDefinition, JSONObject jsonObj, HAPManualDefinitionContextParse parseContext) {}

}
