package com.nosliw.core.application.division.manual.definition;

import org.json.JSONObject;

import com.nosliw.core.application.HAPIdBrickType;
import com.nosliw.core.application.division.manual.HAPManualEnumBrickType;
import com.nosliw.core.application.division.manual.HAPManualManagerBrick;
import com.nosliw.core.xxx.application1.HAPWithValueContext;
import com.nosliw.data.core.runtime.HAPRuntimeEnvironment;

public class HAPManualDefinitionPluginParserBrickImpComplex extends HAPManualDefinitionPluginParserBrickImp{

	public HAPManualDefinitionPluginParserBrickImpComplex(HAPIdBrickType brickTypeId, Class<? extends HAPManualDefinitionBrick> brickClass,
			HAPManualManagerBrick manualDivisionEntityMan, HAPRuntimeEnvironment runtimeEnv) {
		super(brickTypeId, brickClass, manualDivisionEntityMan, runtimeEnv);
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
