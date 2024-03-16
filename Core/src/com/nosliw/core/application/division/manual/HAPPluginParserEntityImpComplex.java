package com.nosliw.core.application.division.manual;

import org.json.JSONObject;

import com.nosliw.core.application.HAPEnumBrickType;
import com.nosliw.core.application.HAPIdBrickType;
import com.nosliw.data.core.common.HAPWithValueContext;
import com.nosliw.data.core.runtime.HAPRuntimeEnvironment;

public class HAPPluginParserEntityImpComplex extends HAPPluginParserEntityImp{

	public HAPPluginParserEntityImpComplex(HAPIdBrickType entityTypeId, Class<? extends HAPManualEntity> entityClass,
			HAPManagerEntityDivisionManual manualDivisionEntityMan, HAPRuntimeEnvironment runtimeEnv) {
		super(entityTypeId, entityClass, manualDivisionEntityMan, runtimeEnv);
	}


	@Override
	protected void parseDefinitionContentJson(HAPManualEntity entityDefinition, Object jsonValue, HAPContextParse parseContext) {
		JSONObject jsonObj = (JSONObject)jsonValue;
		
		parseEntityAttributeJson(entityDefinition, jsonObj, HAPWithValueContext.VALUECONTEXT, HAPEnumBrickType.VALUECONTEXT_100, null, parseContext);	
		
//		this.parseSimpleEntityAttributeJson(jsonObj, entityId, HAPWithAttachment.ATTACHMENT, HAPConstantShared.RUNTIME_RESOURCE_TYPE_ATTACHMENT, null, parserContext);
//		
		this.parseComplexDefinitionContentJson(entityDefinition, jsonObj, parseContext);
	}

	protected void parseComplexDefinitionContentJson(HAPManualEntity entityDefinition, JSONObject jsonObj, HAPContextParse parseContext) {}

}
