package com.nosliw.core.application.division.manual;

import org.json.JSONObject;

import com.nosliw.core.application.HAPIdBrickType;
import com.nosliw.data.core.common.HAPWithValueContext;
import com.nosliw.data.core.runtime.HAPRuntimeEnvironment;

public class HAPPluginParserBrickImpComplex extends HAPPluginParserBrickImp{

	public HAPPluginParserBrickImpComplex(HAPIdBrickType brickTypeId, Class<? extends HAPManualBrick> brickClass,
			HAPManualManagerBrick manualDivisionEntityMan, HAPRuntimeEnvironment runtimeEnv) {
		super(brickTypeId, brickClass, manualDivisionEntityMan, runtimeEnv);
	}


	@Override
	protected void parseDefinitionContentJson(HAPManualBrick brickDefinition, Object jsonValue, HAPManualContextParse parseContext) {
		JSONObject jsonObj = (JSONObject)jsonValue;
		
		parseBrickAttributeJson(brickDefinition, jsonObj, HAPWithValueContext.VALUECONTEXT, HAPManualEnumBrickType.VALUECONTEXT_100, null, parseContext);	
		
//		this.parseSimpleEntityAttributeJson(jsonObj, entityId, HAPWithAttachment.ATTACHMENT, HAPConstantShared.RUNTIME_RESOURCE_TYPE_ATTACHMENT, null, parserContext);
//		
		this.parseComplexDefinitionContentJson(brickDefinition, jsonObj, parseContext);
	}

	protected void parseComplexDefinitionContentJson(HAPManualBrick brickDefinition, JSONObject jsonObj, HAPManualContextParse parseContext) {}

}
